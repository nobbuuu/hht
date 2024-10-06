package com.booyue.net.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author jett
 * @date 2018/6/6
 */

public class RequestCallbacks  implements Callback<String> {
    private final ICallback iCallback;
    public RequestCallbacks(ICallback iCallback) {
        this.iCallback = iCallback;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if(response.isSuccessful()){
            if(call.isExecuted()){
                if(iCallback != null){
                    iCallback.onSuccess(response.body());
                }
            }
        }else{
            if(iCallback != null){
                iCallback.onError(response.code(),response.message());
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if(iCallback != null){
            iCallback.onFailure();
        }
        if(iCallback != null){
            iCallback.onPostExecute();
        }
    }
}
