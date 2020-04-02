package callbacks;

public interface DatabaseOperationNotifier {

    void onSavePerformed(boolean isCompletedSuccessfully);
    void onCurrencyRetrivePerformed(boolean isSuccess);
}
