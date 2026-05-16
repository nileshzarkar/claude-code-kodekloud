package com.auth.service;

import com.auth.dto.AuthResponse;
import com.auth.dto.MessageResponse;
import com.auth.dto.UserInfoResponse;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.security.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Transactional
    public MessageResponse register(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }
        email = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }

        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email is already registered");
        }

        User user = new User();
        user.email = email;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        user.isVerified = false;
        user.verificationToken = UUID.randomUUID().toString();

        userRepository.persist(user);

        // Simulate email: print verification link to console
        String link = "http://localhost:8080/api/auth/verify-email?token=" + user.verificationToken;
        System.out.println("\n=============================================================");
        System.out.println("  SIMULATED EMAIL — Email Verification");
        System.out.println("  To: " + user.email);
        System.out.println("  Click to verify: " + link);
        System.out.println("=============================================================\n");

        return new MessageResponse("Registration successful! Check the backend console for the verification link.");
    }

    @Transactional
    public MessageResponse verifyEmail(String token) {
        if (token == null || token.isBlank()) {
            throw new ValidationException("Verification token is required");
        }

        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isEmpty()) {
            throw new ValidationException("Invalid or expired verification token");
        }

        User user = userOpt.get();

        if (user.isVerified) {
            return new MessageResponse("Email is already verified. You can login.");
        }

        user.isVerified = true;
        user.verificationToken = null;

        return new MessageResponse("Email verified successfully! You can now login.");
    }

    public AuthResponse login(String email, String password) {
        if (email == null || password == null) {
            throw new ValidationException("Email and password are required");
        }
        email = email.trim().toLowerCase();

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || !BCrypt.checkpw(password, userOpt.get().password)) {
            throw new AuthException("Invalid email or password");
        }

        User user = userOpt.get();

        if (!user.isVerified) {
            throw new AuthException("Please verify your email before logging in. Check the backend console for the link.");
        }

        String token = jwtUtil.generateToken(user.id, user.email);
        return new AuthResponse(token, user.email, user.id);
    }

    public UserInfoResponse getMe(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AuthException("User not found");
        }
        return new UserInfoResponse(user.id, user.email, user.isVerified);
    }

    // ---- Exception types ----

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class AuthException extends RuntimeException {
        public AuthException(String message) {
            super(message);
        }
    }
}
