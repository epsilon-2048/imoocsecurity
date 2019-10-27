package com.imooc.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 处理用户信息获取逻辑
 * 根据用户名获取用户信息，如果没有，抛出用户不存在异常
 * 这里不负责密码的校验，密码的校验由spring来校验，你只要提供正确的密码
 */
@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查找用户信息（任何你需要的手段,数据库、xml等）
        //。。。
        //返回一个实现了UserDetails接口的类即可，User是spring默认实现的类，可以自定义一个你需要的类
        System.out.println(username);

        //这个加密方法的调用应该是在注册时将密码加密后放进数据库或XML的，而非在这里调用
        String dbPwd = passwordEncoder.encode("123456");
        //这个密码应该是从数据库或xml中取出的密码，而且是加密过的，而非写死。
        //这里是简单模拟从数据库获取到加密的密码
        String password = dbPwd;
        System.out.println(password);
        //权限列表也是由数据库或xml中获取，多个权限由,分隔
        String authorities = "admin,superAdmin";

        /**
         * 实现你的校验逻辑
         * boolean isAccountNonExpired();
         * 账户是否过期
         * boolean isAccountNonLocked();
         * 账号是否锁定，在业务中一般用来表示账户是否被冻结
         * boolean isCredentialsNonExpired();
         * 密码是否过期，有些需求是要定时更改密码
         * boolean isEnabled();
         * 账户是否可用，一般用来表示账户是否被删除，
         * 一般数据库是不删数据的，
         * 用标志码表示该用户是否被删除（逻辑删除），便可使用此方法表示
         */
        return new User(username, password,
                true, true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
    }
}
