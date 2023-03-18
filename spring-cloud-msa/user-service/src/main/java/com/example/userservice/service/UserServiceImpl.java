package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    private final Environment env;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }


    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDto.class);
    }


    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null){
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);

//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//
//        List<ResponseOrder> body = orderListResponse.getBody();

        
//        List<ResponseOrder> body = null;
//        try {
//            body = orderServiceClient.getOrdersNg(userId);
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }
        /** FeignErrorDecoder로 예외처리 */
        List<ResponseOrder> body = orderServiceClient.getOrders(userId);

//        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(body);


        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDto getUserDetailsByEmail(String userName) {
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return modelMapper.map(userEntity, UserDto.class);
    }
}






