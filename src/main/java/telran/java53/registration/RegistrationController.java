package telran.java53.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.java53.accounting.dto.UserDto;
import telran.java53.accounting.dto.UserRegisterDto;

//@RestController
//@RequestMapping(path = "/account/register")
//@AllArgsConstructor
//public class RegistrationController {
//
//    private final RegistrationService registrationService;
//
//    @PostMapping
//    public String register(@RequestBody RegistrationRequest request) {
//        return registrationService.register(request);
//    }
//
//    @GetMapping(path = "confirm")
//    public String confirm (@RequestParam("token") String token) {
//        return registrationService.confirmToken(token);
//    }
//}
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return registrationService.register(userRegisterDto);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
