# 기술 스택
- 언어 : Java 17
- 프레임워크 : Spring Boot 3.4.2
- DB : H2 DataBase
- 문서화 : Swagger


# API 구현 설명
1. 헬스 체크 API
  - @RestController와 @GetMapping("/health")을 사용하여 간단한 상태 체크 엔드포인트를 제공합니다.
  - 응답은 ApiResponse 객체를 사용하여 200 OK 상태와 함께 메시지를 반환합니다.

2. 초기화 API
  - H2 Database의 모든 데이터를 deleteAll()을 사용하여 초기화합니다.
  - WebClient를 이용해 Faker API에서 유저 데이터를 가져옵니다.
  - 받은 데이터를 User 엔티티로 변환 후 JPA를 통해 저장합니다.
  - fakerId 중복을 방지하기 위해 existsByFakerId()를 사용하여 체크 후 저장합니다.

3. 유저 전체 조회 API
  - Pageable을 활용하여 PageRequest.of(page, size, Sort.by("id").ascending())로 데이터를 가져옵니다.
  - UserResponse DTO를 사용하여 응답 형식을 맞췄습니다.

4. 방 생성 API
  - Room.create() 팩토리 메서드를 활용하여 객체를 생성하고 저장합니다.
  - userRepository.findById()를 사용하여 유저 상태를 확인한 후 생성 여부를 결정합니다.

5. 방 전체 조회 API
   - findAll(Pageable)을 사용하여 정렬된 데이터를 반환합니다.
     
6. 방 상세 조회 API
   - findById()로 방을 조회하고 DTO로 변환하여 응답합니다.
     
7. 방 참가 API
  - isFull(room), isJoinedRoom(user)을 체크 후 참가 가능 여부를 판단합니다.
  - 팀 배정은 RED → BLUE 순서로 이루어집니다.
    
8. 방 나가기 API
  - userRoomRepository.deleteByUserIdAndRoomId()를 사용하여 참가 정보를 삭제합니다.
  - 호스트가 나갈 경우 방 상태를 FINISH로 변경합니다.
    
9. 게임 시작 API
  - room.startGame()을 호출하여 PROGRESS 상태로 변경합니다.
  - @Scheduled을 사용하여 1분 후 자동으로 FINISH 상태로 변경됩니다.
    
10. 팀 변경 API
    - userRoom.changeTeam()을 호출하여 변경된 값을 저장합니다.
