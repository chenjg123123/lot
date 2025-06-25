package com.example.userservice.controller;
import com.example.model.common.R;
import com.example.model.user.Users;
import com.example.service.util.JwtUtil;
import com.example.userservice.requestDTO.LoginRequest;
import com.example.userservice.service.UserService;
import com.example.userservice.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class auth {

    @Autowired
    private RsaUtil rsaUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;  // 不需要 new

    /**
     * 登录
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R auth(@RequestBody LoginRequest request) {
        String encryptedUsername = request.getUsername();
        String encryptedPassword = request.getPassword();
        String username = rsaUtil.decrypt(encryptedUsername);
        String password = rsaUtil.decrypt(encryptedPassword);
        Users login = userService.Login(username, password);
        if (login == null) {
            return R.error("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(login);
        return R.success("登录成功", token);
    }

}
