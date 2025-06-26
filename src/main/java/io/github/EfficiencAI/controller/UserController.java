package io.github.EfficiencAI.controller;

import io.github.EfficiencAI.pojo.VO.ConversationRequestVO;
import io.github.EfficiencAI.pojo.VO.Result;
import io.github.EfficiencAI.pojo.VO.UserVO;
import io.github.EfficiencAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 用户管理接口
    @PostMapping("/create")
    public Mono<Result> createUser(@RequestBody UserVO userVO) {
        return userService.createUser(userVO.toDTO())
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }
    
    @GetMapping("/get")
    public Mono<Result> getUser(@RequestParam String userId) {
        return userService.getUser(userId)
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }
    
    @PutMapping("/update")
    public Mono<Result> updateUser(@RequestBody UserVO userVO) {
        return userService.updateUser(userVO.toDTO())
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }
    
    @DeleteMapping("/delete")
    public Mono<Result> deleteUser(@RequestParam String userId) {
        return userService.deleteUser(userId)
                .map(result ->
                        result.ifSuccess ?
                                Result.success("用户删除成功") :
                                Result.error(result.note));
    }
    
    // 会话管理接口
    @PostMapping("/session/create")
    public Mono<Result> createSession(@RequestParam String userId, @RequestParam String sessionName) {
        return userService.createSession(userId, sessionName)
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }
    
    @GetMapping("/session/get")
    public Mono<Result> getSession(@RequestParam String userId, @RequestParam String sessionName) {
        return userService.getSession(userId, sessionName)
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }

    @GetMapping("/session/getAllSessionsName")
    public Mono<Result> getAllSessionsName(@RequestParam String userId) {
        return userService.getAllSessionsName(userId)
               .map(result ->
                        result.ifSuccess?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }

    @PutMapping("/session/update")
    public Mono<Result> updateSession(@RequestParam String userId, 
                                    @RequestParam String sessionName, 
                                    @RequestParam String newSessionName) {
        return userService.updateSession(userId, sessionName, newSessionName)
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }
    
    @DeleteMapping("/session/delete")
    public Mono<Result> deleteSession(@RequestParam String userId, @RequestParam String sessionName) {
        return userService.deleteSession(userId, sessionName)
                .map(result ->
                        result.ifSuccess ?
                                Result.success("会话删除成功") :
                                Result.error(result.note));
    }
    
    // 对话节点管理接口
    @PostMapping("/conversation/add")
    public Flux<String> addConversationNode(@RequestBody ConversationRequestVO conversationRequestVO) {
        System.out.println(conversationRequestVO);
        return userService.addConversationNode(
            conversationRequestVO.toNodeRequestDTO(),
            conversationRequestVO.toChatRequestDTO()
        );
    }
    
    @PutMapping("/conversation/update")
    public Flux<String> updateConversationNode(@RequestBody ConversationRequestVO conversationRequestVO) {
        return userService.updateConversationNode(
            conversationRequestVO.toNodeRequestDTO(),
            conversationRequestVO.toChatRequestDTO()
        );
    }
    
    @DeleteMapping("/conversation/delete/{conversationNodeId}")
    public Mono<Result> deleteConversationNode(@RequestParam String userId,
                                              @RequestParam String sessionName,
                                              @PathVariable String conversationNodeId) {
        return userService.deleteConversationNode(userId, sessionName, conversationNodeId)
                .map(result ->
                        result.ifSuccess ?
                                Result.success("对话节点删除成功") :
                                Result.error(result.note));
    }
    
    @GetMapping("/conversation/get/{conversationNodeId}")
    public Mono<Result> getConversationNode(@RequestParam String userId,
                                           @RequestParam String sessionName,
                                           @PathVariable String conversationNodeId) {
        return userService.getConversationNode(userId, sessionName, conversationNodeId)
                .map(result ->
                        result.ifSuccess ?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }

    @GetMapping("/conversation/getAllConversationNodesId")
    public Mono<Result> getAllConversationNodesId(@RequestParam String userId,
                                                 @RequestParam String sessionName) {
        return userService.getAllConversationNodesID(userId, sessionName)
              .map(result ->
                        result.ifSuccess?
                                Result.success(result.returnValue) :
                                Result.error(result.note));
    }

}
