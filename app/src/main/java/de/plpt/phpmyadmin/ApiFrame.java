package de.plpt.phpmyadmin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Pascal on 17.04.2016.
 */
public class ApiFrame<T> {

    //region VarDefs
    @SerializedName("data")
    private T data;

    @SerializedName("status")
    private String status;

    @SerializedName("elapsedTime")
    private Double elapsedTime;

    @SerializedName("errorData")
    private ErrorData errorData;
    //endregion

    //region Constructor
    public ApiFrame(JsonObject result,Type cls) {
        Type collectionType = new TypeToken<ApiFrame<T>>() {
        }.getType();


        ApiFrame<T> userApiFrame = (ApiFrame<T>) new Gson().fromJson(result, collectionType);


        if(!userApiFrame.hasErrorData())
        this.data = new Gson().fromJson(result.getAsJsonObject("data"), cls);//Parse data separate because of NullNode and JAva Generic var as Object and not as Type
        this.status = userApiFrame.status;
        this.elapsedTime = userApiFrame.elapsedTime;
        this.errorData = userApiFrame.errorData;

    }
    //endregion


    //region VarGetters
    public String getStatus() {
        return status;
    }

    public Double getElapsedTime() {
        return elapsedTime;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public boolean hasErrorData()
    {
        return errorData!=null;
    }

    public T getData() {
        return data;
    }
    //endregion


}

    class ErrorData
    {
        //region VarDefs
        @SerializedName("errorCode")
        private int  errorCode;

        @SerializedName("errorDetails")
        private String  errorDetails;


        @SerializedName("errorMessage")
        private String  errorMessage;


        @SerializedName("function")
        private String  function;


        @SerializedName("information")
        private String  information;
        //endregion

        @Override
        public String toString()
        {
            return String.format("(%s) %s  /   %s",errorCode,errorDetails,information);
        }

    }


