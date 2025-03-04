package ru.itmentor.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserErrorResponse;
import ru.itmentor.spring.boot_security.demo.util.UserIdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    @ResponseBody
    public List<UserDto> GetAllUsers() {
        return userService.getAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserDto findById(@PathVariable("id") long id) {
        return convertToUserDto(userService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto userDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.add(convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public String updateUser(@RequestBody UserDto userDto,
                             BindingResult bindingResult,
                             @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "edit";

        userService.update(id, convertToUser(userDto));
        return "redirect:/admin/users";
    }

    @DeleteMapping("del/{id}")
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserIdNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("User Not Found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}