package callbacks;

public interface ServerResponseNotifier {

    void onServerResponseRecieved(String response, int resultCode, boolean useResponseDirectly);
}
