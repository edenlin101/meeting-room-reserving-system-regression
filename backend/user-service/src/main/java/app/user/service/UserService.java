package app.user.service;

import app.user.api.user.LoginUserRequest;
import app.user.api.user.LoginUserResponse;
import app.user.api.user.RegisterUserRequest;
import app.user.api.user.RegisterUserResponse;
import app.user.api.user.UserRoleView;
import app.user.api.user.UserStatusView;
import app.user.domain.User;
import app.user.domain.UserRole;
import app.user.domain.UserStatus;
import core.framework.crypto.Hash;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.web.exception.ConflictException;
import core.framework.web.exception.NotFoundException;
import core.framework.web.exception.UnauthorizedException;

public class UserService {
    @Inject
    Repository<User> userRepository;

    public RegisterUserResponse register(RegisterUserRequest request) {
        if (!userRepository.select("username = ?", request.username).isEmpty()) {
            throw new ConflictException("username already exists");
        }

        User user = new User();
        user.username = request.username;
        user.password = Hash.md5Hex(request.password); // In real app use stronger hash like bcrypt, for example use Hash.md5Hex or similar from core.framework
        user.companyId = request.companyId;
        user.status = UserStatus.PENDING;
        user.role = UserRole.USER;
        
        user.id = userRepository.insert(user).orElseThrow();

        RegisterUserResponse response = new RegisterUserResponse();
        response.id = user.id;
        response.username = user.username;
        return response;
    }

    public LoginUserResponse login(LoginUserRequest request) {
        User user = userRepository.select("username = ?", request.username).stream().findFirst()
            .orElseThrow(() -> new UnauthorizedException("invalid username or password"));
            
        if (!user.password.equals(Hash.md5Hex(request.password))) {
            throw new UnauthorizedException("invalid username or password");
        }
        
        if (user.status != UserStatus.ACTIVE) {
            throw new UnauthorizedException("user is not active");
        }
        
        LoginUserResponse response = new LoginUserResponse();
        response.id = user.id;
        response.username = user.username;
        response.companyId = user.companyId;
        response.role = user.role == UserRole.ADMIN ? UserRoleView.ADMIN : UserRoleView.USER;
        return response;
    }
    
    public void updateStatus(Long id, UserStatusView statusView) {
        User user = userRepository.get(id).orElseThrow(() -> new NotFoundException("user not found"));
        user.status = UserStatus.valueOf(statusView.name());
        userRepository.partialUpdate(user);
    }
}