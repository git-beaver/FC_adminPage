package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.User;
import com.example.study.model.enumClass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.repository.UserRepository;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    // 1. request data 가져오기
    // 2. user 생성
    // 3. 생성된 데이터로 UserApiResponse 가져와 return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        UserApiRequest userApiRequest = request.getData();

        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();
        User newUser = baseRepository.save(user);

        return response(newUser);
    }

    @Override
    public Header<UserApiResponse> read(Long id) {

/*        // id -> repository getOne, getById
        Optional<User> optional = userRepository.findById(id);

        // user -> userApiResponse return
        return optional //user가 있다면
                .map(user -> response(user)) //user를 받아 Header의 UserApiResponse로 return
                .orElseGet( //user가 없다면 헤더에 에러를 넘기면서 데이터 없음 출력
                        ()->Header.ERROR("데이터 없음")
                );
                아래와 동일
                */

        return baseRepository.findById(id)
                .map(user -> response(user))
                .orElseGet(
                        ()->Header.ERROR("데이터 없음")
                );
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        // 1. data 가져오기
        UserApiRequest userApiRequest = request.getData();

        // 2. id -> user 데이터 찾기
        Optional<User> optional = baseRepository.findById(userApiRequest.getId());

        return optional.map(user -> {
            // 3. data -> update
            user.setAccount(userApiRequest.getAccount())
                    .setPassword(userApiRequest.getPassword())
                    .setStatus(userApiRequest.getStatus())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt());
            return user;
        })
        .map(user -> baseRepository.save(user))  // update -> update된 새 유저 번환
        .map(updateUser -> response(updateUser)) //userApiResponse
        .orElseGet(()->Header.ERROR("데이터 없음"));

    }

    @Override
    public Header delete(Long id) {
        // 1. id -> repository -> user
        Optional<User> optional = baseRepository.findById(id);

        // 2. repository -> delete
        return optional.map(user -> {
            baseRepository.delete(user);
            return Header.OK();
        })
                .orElseGet(()->Header.ERROR(""));

    }

    private Header<UserApiResponse> response(User user) {
        //user 객체로 userApiResponse 만들어 return
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) //todo 암호화, 길이 변경
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        //Header + data return
        return Header.OK(userApiResponse);
    }
}