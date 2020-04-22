package callbacks;

import Model.ApiResponseModel;

public interface ServerResponseNotifier {

    void onServerResponseRecieved(ApiResponseModel response, int resultCode, boolean useResponseDirectly);
}
