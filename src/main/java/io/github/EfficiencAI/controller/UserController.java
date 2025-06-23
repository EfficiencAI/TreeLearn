package io.github.EfficiencAI.controller;

import io.github.EfficiencAI.pojo.VO.NodeRequestVO;
import io.github.EfficiencAI.pojo.VO.Result;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/add")
    public Flux<String> addNode(@RequestBody NodeRequestVO nodeRequestVO) {
        return null;
    }

    @DeleteMapping("/delete/{nodeId}")
    public Result deleteNode(@PathVariable String nodeId) {
        return Result.success();
    }

    @PutMapping("/update")
    public Flux<String> updateNode(@RequestBody NodeRequestVO nodeRequestVO) {
        return null;
    }
}
