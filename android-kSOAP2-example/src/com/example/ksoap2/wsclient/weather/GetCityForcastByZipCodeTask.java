package com.example.ksoap2.wsclient.weather;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.example.ksoap2.wsclient.AbstractProgressableAsyncTask;
import com.example.ksoap2.wsclient.weather.bo.CityForecastBO;
import com.example.ksoap2.wsclient.weather.bo.CityForecastBO.ForecastInfo;

public class GetCityForcastByZipCodeTask extends AbstractProgressableAsyncTask<SoapObject, CityForecastBO> {

    private static final String TAG = "GetCityForcastByZipCodeTask";

    private static final String WSDL_URL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx?WSDL";
    private static final String WS_NAMESPACE = "http://ws.cdyne.com/WeatherWS/";
    private static final String WS_METHOD_NAME = "GetCityForecastByZIP";

    public GetCityForcastByZipCodeTask() {

    }

    public static SoapObject createRequest(String zipCode) {
        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo property = new PropertyInfo();
        property.setNamespace(WS_NAMESPACE); // to ensure that the element-name is prefixed with the namespace
        property.setName("ZIP");
        property.setValue(zipCode);
        
        request.addProperty(property);
        
        return request;
    }

    @Override
    protected CityForecastBO performTaskInBackground(SoapObject parameter) throws Exception {
        // 1. Create SOAP Envelope using the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(parameter);

        // 2. Create a HTTP Transport object to send the web service request
        HttpTransportSE httpTransport = new HttpTransportSE(WSDL_URL);
        httpTransport.debug = true; // allows capture of raw request/respose in Logcat

        // 3. Make the web service invocation
        httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);

        Log.d(TAG, "HTTP REQUEST:\n" + httpTransport.requestDump);
        Log.d(TAG, "HTTP RESPONSE:\n" + httpTransport.responseDump);

        CityForecastBO result = null;
        if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            result = parseSOAPResponse(soapObject);
        } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new Exception(soapFault.getMessage());
        }

        return result;
    }

    private CityForecastBO parseSOAPResponse(SoapObject response) {
        CityForecastBO cityForecastResult = null;
        SoapObject cityForecastNode = (SoapObject) response.getProperty("GetCityForecastByZIPResult");
        // see the wsdl for the definition of "ForecastReturn" (which can be null/empty)
        // i.e. <s:element name="GetCityForecastByZIPResponse"> element 
        if (cityForecastNode != null) {
            // see <s:complexType name="ForecastReturn"> element for definition of "ForecastReturn"
            
            // "Success" is a mandatory node, so no need to do any null checks.
            boolean isSuccess = Boolean.parseBoolean(cityForecastNode.getPrimitivePropertySafelyAsString("Success"));
                    
            String responseText =  cityForecastNode.getPrimitivePropertySafelyAsString("ResponseText");
            String state =  cityForecastNode.getPrimitivePropertySafelyAsString("State");
            String city =  cityForecastNode.getPrimitivePropertySafelyAsString("City");
            String weatherStationCity =  cityForecastNode.getPrimitivePropertySafelyAsString("WeatherStationCity");
            cityForecastResult = new CityForecastBO(isSuccess, state, city, weatherStationCity, responseText);
            

            Log.i(TAG, String.format(" --> isSuccess: %b, responseText: %s, state: %s, city: %s, weatherStationCity: %s.", isSuccess, responseText, state, city, weatherStationCity));
            // "ForecastResult" (an array) can be empty according to the wsdl definition. Therefore, we do a null check before processing
            // any data.
            SoapObject forecastResultsNode = (SoapObject) cityForecastNode.getPropertySafely("ForecastResult");
            if (forecastResultsNode != null) {
                // Definition for Forecast node can be found at <s:complexType name="Forecast"> element in wsdl
                for (int i = 0; i < forecastResultsNode.getPropertyCount(); i++) {
                    SoapObject forecastNode = (SoapObject) forecastResultsNode.getProperty(i);
                    
                    String date =  forecastNode.getPrimitivePropertySafelyAsString("Date");
                    short weatherId =  Short.parseShort(forecastNode.getPrimitivePropertySafelyAsString("WeatherID"));
                    // description can be null
                    //      <s:element minOccurs="0" maxOccurs="1" name="Desciption" type="s:string"/>
                    String description =  forecastNode.getPrimitivePropertySafelyAsString("Desciption");
                    ForecastInfo forecast = cityForecastResult.addForecast(date, weatherId, description);
                    
                    // "Temperatures" and "ProbabilityOfPrecipiation" nodes are mandatory within a "Forecast" node
                    //      <s:element minOccurs="1" maxOccurs="1" name="Temperatures" type="tns:temp"/>
                    //      <s:element minOccurs="1" maxOccurs="1" name="ProbabilityOfPrecipiation" type="tns:POP"/>
                    SoapObject temperatureNode = (SoapObject)forecastNode.getProperty("Temperatures");
                    String moringLowTemp =  temperatureNode.getPrimitivePropertySafelyAsString("MorningLow");
                    String daytimeHighTemp =  temperatureNode.getPrimitivePropertySafelyAsString("DaytimeHigh");
                    forecast.setTemperature(moringLowTemp, daytimeHighTemp);
                    
                    SoapObject precipicationProbabilityNode = (SoapObject)forecastNode.getProperty("ProbabilityOfPrecipiation");
                    String nightTimePercentage =  precipicationProbabilityNode.getPrimitivePropertySafelyAsString("Nighttime");
                    String dayTimePercentage =  precipicationProbabilityNode.getPrimitivePropertySafelyAsString("Daytime");
                    forecast.setPrecipiationProbability(nightTimePercentage, dayTimePercentage);
                    
                    Log.i(TAG, String.format(" --> [%d] ID: %d, desc: %s, date: %s, Temp [%s-%s], Percipitation: %s-%s.", i, weatherId, description, date, moringLowTemp,
                            daytimeHighTemp, nightTimePercentage, dayTimePercentage));
                }
            }
        }

        return cityForecastResult;
    }

}
