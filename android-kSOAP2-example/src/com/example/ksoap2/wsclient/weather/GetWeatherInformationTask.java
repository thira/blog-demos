package com.example.ksoap2.wsclient.weather;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.example.ksoap2.wsclient.AbstractProgressableAsyncTask;
import com.example.ksoap2.wsclient.weather.bo.WeatherDescriptionBO;

public class GetWeatherInformationTask extends AbstractProgressableAsyncTask<SoapObject, List<WeatherDescriptionBO>> {

    private static final String TAG = "GetWeatherInformationTask";

    private static final String WSDL_URL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx?WSDL";
    private static final String WS_NAMESPACE = "http://ws.cdyne.com/WeatherWS/";
    private static final String WS_METHOD_NAME = "GetWeatherInformation";

    public GetWeatherInformationTask() {

    }

    public static SoapObject createRequest() {
        return new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);
    }

    @Override
    protected List<WeatherDescriptionBO> performTaskInBackground(SoapObject parameter) throws Exception {
        // 1. Create SOAP Envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        // 2. Set the request parameters
        envelope.setOutputSoapObject(parameter);

        // 3. Create a HTTP Transport object to send the web service request
        HttpTransportSE httpTransport = new HttpTransportSE(WSDL_URL);
        httpTransport.debug = true; // allows capture of raw request/respose in Logcat

        // 4. Make the web service invocation
        httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);

        Log.d(TAG, "HTTP REQUEST:\n" + httpTransport.requestDump);
        Log.d(TAG, "HTTP RESPONSE:\n" + httpTransport.responseDump);

        List<WeatherDescriptionBO> result = new ArrayList<WeatherDescriptionBO>();
        if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            result = parseSOAPResponse(soapObject);
        } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new Exception(soapFault.getMessage());
        }

        return result;
    }

    private List<WeatherDescriptionBO> parseSOAPResponse(SoapObject response) {
        List<WeatherDescriptionBO> result = new ArrayList<WeatherDescriptionBO>();
        SoapObject weatherInfoResult = (SoapObject) response.getProperty("GetWeatherInformationResult");
        if (weatherInfoResult != null) {

            for (int i = 0; i < weatherInfoResult.getPropertyCount(); i++) {
                SoapObject weatherDescription = (SoapObject) weatherInfoResult.getProperty(i);
                int weatherId = Integer.parseInt(weatherDescription.getPrimitivePropertySafelyAsString("WeatherID"));
                String description = weatherDescription.getPrimitivePropertySafelyAsString("Description");
                String pictureUrl = weatherDescription.getPrimitivePropertySafelyAsString("PictureURL");

                Log.i(TAG, String.format(" --> ID: %d, desc: %s, url: %s.", weatherId, description, pictureUrl));
                result.add(new WeatherDescriptionBO(weatherId, description, pictureUrl));
            }
        }

        return result;
    }

}
