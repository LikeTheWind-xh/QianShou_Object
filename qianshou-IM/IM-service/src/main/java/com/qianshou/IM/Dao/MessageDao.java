package com.qianshou.IM.Dao;

import com.mongodb.client.result.UpdateResult;

import com.qianshou.pojo.Message;
import org.bson.types.ObjectId;

import java.util.List;

public interface MessageDao {
    /**
     * 查询聊天记录
     * @param fromId
     * @param toId
     * @param page
     * @param rows
     * @return
     */
    List<Message> findByFromAndTo(Long fromId, Long toId, Integer page, Integer rows);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    Message findMessageById(String id);

    Message queryList(Long fromId , Long toId);

    /**
     * 更改消息状态
     * @param id
     * @param status
     * @return
     */
    UpdateResult updateMessageState(ObjectId id,Integer status);

    List<Message> queryUserList(Long fromId, Long toId);

    /**
     * 新增消息
     * @param message
     * @return
     */
    Message saveMessage(Message message);

}
