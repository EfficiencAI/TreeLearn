package io.github.EfficiencAI.pojo.VO;

public class NodeOperationResult<T> {
    public enum OperationType {
        CREATE,
        GET,
        MODIFY,
        DELETE
    }
    public NodeOperationResult(OperationType operationType, T returnValue, boolean ifSuccess, String note) {
        this.operationType = operationType;
        this.returnValue = returnValue;
        this.ifSuccess = ifSuccess;
        this.note = note;
    }
    public OperationType operationType = OperationType.CREATE;
    public T returnValue = null;
    public boolean ifSuccess = false;
    public String note = "";
}
