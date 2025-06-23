package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.Entites.node.Base.PersistentNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;

public class NodeOperationResult<T extends PersistentNode> {
    public enum OperationType {
        CREATE,
        GET,
        MODIFY,
        DELETE
    }
    public NodeOperationResult(OperationType operationType, T node, boolean ifSuccess, String note) {
        this.operationType = operationType;
        this.node = node;
        this.ifSuccess = ifSuccess;
        this.note = note;
    }
    public OperationType operationType = OperationType.CREATE;
    public T node = null;
    public boolean ifSuccess = false;
    public String note = "";
}
