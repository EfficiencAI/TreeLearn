package io.github.EfficiencAI.pojo.Entites.node.Base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.EfficiencAI.utils.FileOperationUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public abstract class PersistentNode {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取节点的唯一标识符，用于生成存储文件名。
     * @return 节点的唯一标识符
     */
    protected abstract String getIdentifier();

    /**
     * 将当前节点实例序列化为 JSON 并存储到指定路径的文件中。
     * @param storageFolderPath 存储节点 JSON 文件的文件夹路径
     * @return 若序列化和存储成功返回 true，否则返回 false
     */
    public boolean saveSelfToFile(String storageFolderPath) {
        // 检查存储目录是否存在，如果不存在则创建
        String filePath = storageFolderPath + "/" + getIdentifier() + ".json";
        FileOperationUtil.makeSurePathExists(storageFolderPath);

        // 检查文件是否存在，如果不存在则创建
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                return false;
            }
        }

        // 写入文件
        try {
            mapper.writeValue(file, this);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

    protected abstract boolean cascadeLoad();

    /**
     * 从指定路径的文件中反序列化 JSON 数据并创建节点实例。
     * @param storageFilePath 存储节点 JSON 文件的完整路径
     * @param clazz 节点类的 Class 对象
     * @param <T> 节点类的类型
     * @return 反序列化成功返回节点实例，失败返回 null
     */
    public static <T extends PersistentNode> T loadFromFile(String storageFilePath, Class<T> clazz) {
        // 检查文件是否存在
        File file = new File(storageFilePath);
        if (!file.exists()) {
            return null;
        }

        // 读取文件
        try {
            T node = mapper.readValue(file, clazz);
            if (node != null) {
                if(!node.cascadeLoad()){
                    return null;
                }
            }
            return node;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return null;
        }


    }

    protected abstract boolean cascadeDelete();

    public boolean deleteSelfFromFile(String storageFolderPath) {
        boolean ifAllDeleteOperationExecuteSucceed = true;
        String filePath = storageFolderPath + "/" + getIdentifier() + ".json";
        File file = new File(filePath);
        if (!file.exists()) {
            ifAllDeleteOperationExecuteSucceed = false;
            return ifAllDeleteOperationExecuteSucceed;
        }
        if(!cascadeDelete()){
            ifAllDeleteOperationExecuteSucceed = false;
        }
        if(!file.delete()){
            ifAllDeleteOperationExecuteSucceed = false;
            return ifAllDeleteOperationExecuteSucceed;
        }
        return ifAllDeleteOperationExecuteSucceed;
    }
}