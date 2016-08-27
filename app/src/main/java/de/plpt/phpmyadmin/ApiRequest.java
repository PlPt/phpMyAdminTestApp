package de.plpt.phpmyadmin;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pascal on 30.07.2016.
 */
@ApiUrl(BaseUrl = "http://192.168.0.7/apis/phpMyAdmin/api",URL="")
public class ApiRequest {

    //region InterfaceDefinition
    public interface  ApiResponse
    {
        void onFinish(Exception x,ApiFrame<?> frame);
    }
    //endregion

    //region VarDefs
    private Context ctx;
    //endregion

    //region Constructor and Static constructor
    public ApiRequest(Context c)
    {
        this.ctx = c;
    }

    public static ApiRequest newInstance(Context ctx)
    {
        return new ApiRequest(ctx);
    }
    //endregion

    //region public API Methods

    /**
     * Get (list) all DataBases on a server
     * @param res ApiResponse Callback
     */
    @ApiUrl(URL="/db/all")
    public void getDatabases(final ApiResponse res)
    {
        String url = buildUrl(getClassAnnotation().BaseUrl(),getAnnotation().URL());
        Ion.with(ctx).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if(e!=null)
                {
                    res.onFinish(e,null);
                    return;
                }
                ApiFrame<DataTable> userApiFrame = new ApiFrame<DataTable>(result, DataTable.class);
                res.onFinish(null,userApiFrame);
            }

        });

    }

    /**
     * Get (list) the tables for a specific database
     * @param dbName databaseName
     * @param res ApiResonse Callback
     */
    @ApiUrl(URL="/db/%s/tables")
    public void getTables(String dbName,final ApiResponse res)
    {
        String url = buildUrl(getClassAnnotation().BaseUrl(),getAnnotation().URL(),dbName);
        Ion.with(ctx).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if(e!=null)
                {
                    res.onFinish(e,null);
                    return;
                }
                ApiFrame<DataTable> userApiFrame = new ApiFrame<DataTable>(result, DataTable.class);
                res.onFinish(null,userApiFrame);
            }

        });

    }

    /**
     * Executes a SQL Query
     * @param dbName Database name to execute
     * @param query Query string
     * @param res ApiResponse Callback
     */
    @ApiUrl(URL="/api/%s/query")
    public void query(String dbName,String query,final ApiResponse res)
    {
        String url = buildUrl(getClassAnnotation().BaseUrl(),getAnnotation().URL(),dbName);

        Ion.with(ctx).load(url).addHeaders(createPostParameters(new String[]{"sql",query})).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if(e!=null)
                {
                    res.onFinish(e,null);
                    return;
                }
                ApiFrame<DataTable> userApiFrame = new ApiFrame<DataTable>(result, DataTable.class);
                res.onFinish(null,userApiFrame);
            }

        });

    }

    //endregion


    //region privateMethods

    /***
     * Builds the Url from Base and relative Strings from annotation
     * @param base Base url
     * @param relativ relative url
     * @param params formatter params
     * @return complete url string
     */
    private String buildUrl(String base,String relativ,Object... params)
    {
        return base + String.format(relativ,params);
    }

    /***
     * Returns the current Method Annotation
     * @return current Method (above) Annotation
     */
    private ApiUrl getAnnotation() {
        //Method m = this.getClass().getEnclosingMethod();


        Method m = null;
        try {
            m = getCallingMethod();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(m.isAnnotationPresent(ApiUrl.class))
        {
            ApiUrl ta = m.getAnnotation(ApiUrl.class);
            return ta;
        }

        return null;

    }

    /**
     * Creates a HashMap for Parameters oput of a String array
     * @param params paramters to process
     * @return HashMap of KeyValue pairs for use for ION http post
     */
    private static HashMap<String,List<String>> createPostParameters(String[]... params) {

        HashMap<String,List<String>> map = new HashMap<>();

        for(String[] arr : params) {
            List<String> l = new ArrayList<String>();

            l.add(arr[1]);
            map.put(arr[0],l);
        }

        return map;

    }

    /***
     * Returns the MethodObject for the current Calling Method (2 steps above)
     * @return caller Method object
     * @throws ClassNotFoundException exception if something goes wrong...
     */
    private static Method getCallingMethod() throws ClassNotFoundException{
        final Thread t = Thread.currentThread();
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final StackTraceElement ste = stackTrace[2+2];
        final String methodName = ste.getMethodName();
        final String className = ste.getClassName();
        Class<?> kls = Class.forName(className);
        do{
            for(final Method candidate : kls.getDeclaredMethods()){
                if(candidate.getName().equals(methodName)){
                    return candidate;
                }
            }
            kls = kls.getSuperclass();
        } while(kls != null);
        return null;
    }

    /***
     * Gets the Annotation for Specific Class
     * @return Annotation of specific Class
     */
    private ApiUrl getClassAnnotation()
    {
        if(this.getClass().isAnnotationPresent(ApiUrl.class))
        {
            ApiUrl ta = this.getClass().getAnnotation(ApiUrl.class);
            return ta;

        }
        return null;
    }
  //endregion


}
