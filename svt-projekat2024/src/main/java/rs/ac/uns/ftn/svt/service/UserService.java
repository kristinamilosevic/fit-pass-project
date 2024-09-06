package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.dto.UserUpdateDTO;
import rs.ac.uns.ftn.svt.model.Manages;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public String updateUser(String email, UserUpdateDTO userUpdateDTO) {
        System.out.println("Updating user with email: " + email);
        System.out.println("lalal: " + email.trim().equals("newadmin@gmail.com"));

        Optional<User> optionalUser = userRepository.findByEmail(email.trim());
        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User user = optionalUser.get();
        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        }
        if (userUpdateDTO.getSurname() != null) {
            user.setSurname(userUpdateDTO.getSurname());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        }
        if (userUpdateDTO.getAddress() != null) {
            user.setAddress(userUpdateDTO.getAddress());
        }
        if (userUpdateDTO.getUserType() != null) {
            user.setUserType(userUpdateDTO.getUserType());
        }
        if (userUpdateDTO.getCity() != null) {
            user.setCity(userUpdateDTO.getCity());
        }
        if (userUpdateDTO.getZipCode() != null) {
            user.setZipCode(userUpdateDTO.getZipCode());
        }
        if (userUpdateDTO.getBirthday() != null) {
            user.setBirthday(userUpdateDTO.getBirthday());
        }

        userRepository.save(user);

        return "User successfully updated";
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Long findUserIdByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::getId).orElse(null);
    }


    public String findUserTypeByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::getUserType).orElse(null);
    }

    public List<User> findByUserType(String userType) {
        return userRepository.findByUserType(userType);
    }

    public UserUpdateDTO toUserUpdateDTO(User user) {
        if (user == null) {
            return null;
        }

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setBirthday(user.getBirthday());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setUserType(user.getUserType());
        dto.setZipCode(user.getZipCode());

        return dto;
    }
}
