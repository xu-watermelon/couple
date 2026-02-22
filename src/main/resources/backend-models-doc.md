# 情侣互动小程序后端模型文档

## 1. 概述

本文档描述了情侣互动小程序后端需要的实体类、请求DTO（Request Data Transfer Object）和响应DTO（Response Data Transfer Object）。这些模型基于之前生成的API接口文档，用于后端开发参考。

## 2. 基础结构

### 2.1 响应结构

```java
// 基础响应类
public class BaseResponse<T> {
    private String code; // 错误码，0000表示成功
    private T data; // 响应数据
    private String message; // 错误信息，成功时为空
    
    // getters and setters
}

// 分页响应类
public class PaginationResponse<T> {
    private List<T> list; // 数据列表
    private long total; // 总数据量
    private int page; // 当前页码
    private int pageSize; // 每页大小
    private int totalPages; // 总页数
    
    // getters and setters
}
```

## 3. 认证模块（Auth）

### 3.1 实体类

#### User

```java
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email; // 邮箱（替代username作为登录标识）
    
    @Column(nullable = false)
    private String password;
    
    private String nickname;
    private String avatar;
    private Integer gender; // 0: 女, 1: 男
    private LocalDate birthday;
    private Integer hasCouple; // 0: 无情侣, 1: 有情侣
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
    
    // 关联关系
    @OneToOne(mappedBy = "user")
    private Couple couple;
    
    // getters and setters
    // 生命周期方法
}
```

### 3.2 请求DTO

#### PasswordLoginDTO

```java
@Data
public class PasswordLoginDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码必须包含字母和数字")
    private String password;
}
```

#### RegisterDTO

```java
@Data
public class RegisterDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码必须包含字母和数字")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度必须在2-20个字符之间")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,20}$", message = "昵称只能包含中文、字母、数字和下划线")
    private String nickname;

    @NotBlank(message = "头像不能为空")
    private String avatar;

    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别只能是0或1")
    @Max(value = 1, message = "性别只能是0或1")
    private Integer gender;

    @NotNull(message = "生日不能为空")
    private LocalDate birthday;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String captcha;

    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;
}
```

#### RefreshTokenRequest

```java
public class RefreshTokenRequest {
    @NotBlank(message = "刷新token不能为空")
    private String refreshToken;
    
    // getters and setters
}
```

#### ForgotPasswordRequest

```java
public class ForgotPasswordRequest {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    // getters and setters
}
```

#### ResetPasswordRequest

```java
public class ResetPasswordRequest {
    @NotBlank(message = "重置token不能为空")
    private String token;
    
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6-32之间")
    private String password;
    
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
    
    // getters and setters
    // 验证方法
}
```

#### ChangePasswordRequest

```java
public class ChangePasswordRequest {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6-32之间")
    private String newPassword;
    
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
    
    // getters and setters
    // 验证方法
}
```

#### WechatLoginRequest

```java
public class WechatLoginRequest {
    @NotBlank(message = "微信登录code不能为空")
    private String code;
    
    @NotNull(message = "微信用户信息不能为空")
    private WechatUserInfo userInfo;
    
    // getters and setters
}

public class WechatUserInfo {
    private String openId;
    private String nickname;
    private String avatarUrl;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String language;
    
    // getters and setters
}
```

### 3.3 响应DTO

#### LoginResponseVO

```java
@Data
public class LoginResponseVO {
    private String token;
    private UserVO userInfo;
}

@Data
public class UserVO {
    private Long id;
    private String email;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer hasCouple;
    private LocalDate birthday;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

#### RefreshTokenResponse

```java
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
    
    // getters and setters
}
```

#### CaptchaLoginDTO

```java
@Data
public class CaptchaLoginDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String captcha;

    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;
}
```

#### ForgetPasswordDTO

```java
@Data
public class ForgetPasswordDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String captcha;

    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码必须包含字母和数字")
    private String password;
}
```

#### UpdatePasswordDTO

```java
@Data
public class UpdatePasswordDTO {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码必须包含字母和数字")
    private String newPassword;

    @NotBlank(message = "确认新密码不能为空")
    private String confirmNewPassword;
}
```

## 4. 情侣模块（Couple）

### 4.1 实体类

#### Couple

```java
@Entity
@Table(name = "couples")
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "couple_code", nullable = false, unique = true)
    private String coupleCode;
    
    @Column(name = "love_start_date", nullable = false)
    private LocalDate loveStartDate;
    
    @Column(name = "couple_name")
    private String coupleName;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "couple")
    private List<CoupleAnniversary> anniversaries;
    
    @OneToMany(mappedBy = "couple")
    private List<CoupleMemory> memories;
    
    // getters and setters
    // 生命周期方法
}
```

#### CoupleAnniversary

```java
@Entity
@Table(name = "couple_anniversaries")
public class CoupleAnniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "纪念日标题不能为空")
    private String title;
    
    @NotNull(message = "纪念日日期不能为空")
    private LocalDate date;
    
    private String description;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "couple_id", nullable = false)
    private Couple couple;
    
    // getters and setters
    // 生命周期方法
}
```

#### CoupleMemory

```java
@Entity
@Table(name = "couple_memories")
public class CoupleMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "记忆标题不能为空")
    private String title;
    
    @NotNull(message = "记忆日期不能为空")
    private LocalDate date;
    
    @NotBlank(message = "记忆内容不能为空")
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "memory_images", joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "image_url")
    private List<String> images;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "couple_id", nullable = false)
    private Couple couple;
    
    // getters and setters
    // 生命周期方法
}
```

### 4.2 请求DTO

#### BindCoupleRequest

```java
public class BindCoupleRequest {
    @NotBlank(message = "情侣码不能为空")
    private String coupleCode;
    
    @NotBlank(message = "恋爱开始日期不能为空")
    private String loveStartDate; // 格式：yyyy-MM-dd
    
    // getters and setters
}
```

#### UpdateCoupleRequest

```java
public class UpdateCoupleRequest {
    private String loveStartDate; // 格式：yyyy-MM-dd
    private String coupleName;
    
    // getters and setters
}
```

#### CreateAnniversaryRequest

```java
public class CreateAnniversaryRequest {
    @NotBlank(message = "纪念日标题不能为空")
    private String title;
    
    @NotBlank(message = "纪念日日期不能为空")
    private String date; // 格式：yyyy-MM-dd
    
    private String description;
    
    // getters and setters
}
```

#### UpdateAnniversaryRequest

```java
public class UpdateAnniversaryRequest {
    private String title;
    private String date; // 格式：yyyy-MM-dd
    private String description;
    
    // getters and setters
}
```

#### CreateMemoryRequest

```java
public class CreateMemoryRequest {
    @NotBlank(message = "记忆标题不能为空")
    private String title;
    
    @NotBlank(message = "记忆日期不能为空")
    private String date; // 格式：yyyy-MM-dd
    
    @NotBlank(message = "记忆内容不能为空")
    private String content;
    
    private List<String> images;
    
    // getters and setters
}
```

#### UpdateMemoryRequest

```java
public class UpdateMemoryRequest {
    private String title;
    private String date; // 格式：yyyy-MM-dd
    private String content;
    private List<String> images;
    
    // getters and setters
}
```

### 4.3 响应DTO

#### BindCoupleResponse

```java
public class BindCoupleResponse {
    private CoupleInfo coupleInfo;
    
    // getters and setters
}

public class CoupleInfo {
    private Long id;
    private String coupleCode;
    private String loveStartDate;
    private String coupleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PartnerInfo partnerInfo;
    
    // getters and setters
}

public class PartnerInfo {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    
    // getters and setters
}
```

#### GetCoupleInfoResponse

```java
public class GetCoupleInfoResponse {
    private CoupleInfo coupleInfo;
    
    // getters and setters
}
```

#### UpdateCoupleResponse

```java
public class UpdateCoupleResponse {
    private CoupleInfo coupleInfo;
    
    // getters and setters
}
```

#### CreateAnniversaryResponse

```java
public class CreateAnniversaryResponse {
    private AnniversaryDetail anniversary;
    
    // getters and setters
}

public class AnniversaryDetail {
    private Long id;
    private String title;
    private String date;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### UpdateAnniversaryResponse

```java
public class UpdateAnniversaryResponse {
    private AnniversaryDetail anniversary;
    
    // getters and setters
}
```

#### GetCoupleAnniversariesResponse

```java
public class GetCoupleAnniversariesResponse {
    private List<AnniversaryDetail> anniversaries;
    
    // getters and setters
}
```

#### CreateMemoryResponse

```java
public class CreateMemoryResponse {
    private MemoryDetail memory;
    
    // getters and setters
}

public class MemoryDetail {
    private Long id;
    private String title;
    private String date;
    private String content;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### UpdateMemoryResponse

```java
public class UpdateMemoryResponse {
    private MemoryDetail memory;
    
    // getters and setters
}
```

#### GetCoupleMemoriesResponse

```java
public class GetCoupleMemoriesResponse {
    private List<MemoryDetail> memories;
    private long total;
    
    // getters and setters
}
```

## 5. 消息模块（Message）

### 5.1 实体类

#### Message

```java
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    @Column(nullable = false)
    private String type; // 消息类型：text, image, voice, etc.
    
    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean read;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;
    
    // getters and setters
    // 生命周期方法
}
```

#### SpecialMessage

```java
@Entity
@Table(name = "special_messages")
public class SpecialMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "消息标题不能为空")
    private String title;
    
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    @NotBlank(message = "解锁密码不能为空")
    private String password;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "couple_id", nullable = false)
    private Couple couple;
    
    // getters and setters
    // 生命周期方法
}
```

#### TimedMessage

```java
@Entity
@Table(name = "timed_messages")
public class TimedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "消息标题不能为空")
    private String title;
    
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    @Column(name = "unlock_date", nullable = false)
    private LocalDateTime unlockDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "couple_id", nullable = false)
    private Couple couple;
    
    // getters and setters
    // 生命周期方法
}
```

### 5.2 请求DTO

#### SendMessageRequest

```java
public class SendMessageRequest {
    @NotBlank(message = "消息内容不能为空")
    private String content;
    private String type; // 消息类型，默认text
    
    // getters and setters
}
```

#### CreateSpecialMessageRequest

```java
public class CreateSpecialMessageRequest {
    @NotBlank(message = "消息标题不能为空")
    private String title;
    
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    @NotBlank(message = "解锁密码不能为空")
    private String password;
    
    // getters and setters
}
```

#### UnlockSpecialMessageRequest

```java
public class UnlockSpecialMessageRequest {
    @NotNull(message = "消息ID不能为空")
    private Long messageId;
    
    @NotBlank(message = "解锁密码不能为空")
    private String password;
    
    // getters and setters
}
```

#### CreateTimedMessageRequest

```java
public class CreateTimedMessageRequest {
    @NotBlank(message = "消息标题不能为空")
    private String title;
    
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    @NotBlank(message = "解锁日期不能为空")
    private String unlockDate; // 格式：yyyy-MM-dd HH:mm:ss
    
    // getters and setters
}
```

### 5.3 响应DTO

#### GetMessageListResponse

```java
public class GetMessageListResponse {
    private List<MessageDetail> messages;
    private long total;
    
    // getters and setters
}

public class MessageDetail {
    private Long id;
    private String content;
    private String type;
    private Long senderId;
    private Long receiverId;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### SendMessageResponse

```java
public class SendMessageResponse {
    private MessageDetail message;
    
    // getters and setters
}
```

#### UnlockSpecialMessageResponse

```java
public class UnlockSpecialMessageResponse {
    private SpecialMessageDetail message;
    
    // getters and setters
}

public class SpecialMessageDetail {
    private Long id;
    private String title;
    private String content;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### GetSpecialMessageListResponse

```java
public class GetSpecialMessageListResponse {
    private List<SpecialMessageDetail> messages;
    
    // getters and setters
}
```

#### CreateTimedMessageResponse

```java
public class CreateTimedMessageResponse {
    private TimedMessageDetail message;
    
    // getters and setters
}

public class TimedMessageDetail {
    private Long id;
    private String title;
    private String content;
    private String unlockDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### GetTimedMessageListResponse

```java
public class GetTimedMessageListResponse {
    private List<TimedMessageDetail> messages;
    
    // getters and setters
}
```

## 6. 动态模块（Dynamic）

### 6.1 实体类

#### Dynamic

```java
@Entity
@Table(name = "dynamics")
public class Dynamic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "动态内容不能为空")
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "dynamic_images", joinColumns = @JoinColumn(name = "dynamic_id"))
    @Column(name = "image_url")
    private List<String> images;
    
    private String location;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "likes", columnDefinition = "int default 0")
    private int likes;
    
    @Column(name = "comments", columnDefinition = "int default 0")
    private int comments;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @OneToMany(mappedBy = "dynamic")
    private List<DynamicLike> dynamicLikes;
    
    @OneToMany(mappedBy = "dynamic")
    private List<DynamicComment> dynamicComments;
    
    // getters and setters
    // 生命周期方法
}
```

#### DynamicLike

```java
@Entity
@Table(name = "dynamic_likes")
public class DynamicLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dynamic_id", nullable = false)
    private Long dynamicId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "dynamic_id", insertable = false, updatable = false)
    private Dynamic dynamic;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### DynamicComment

```java
@Entity
@Table(name = "dynamic_comments")
public class DynamicComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dynamic_id", nullable = false)
    private Long dynamicId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "dynamic_id", insertable = false, updatable = false)
    private Dynamic dynamic;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private DynamicComment parent;
    
    @OneToMany(mappedBy = "parent")
    private List<DynamicComment> replies;
    
    // getters and setters
    // 生命周期方法
}
```

### 6.2 请求DTO

#### CreateDynamicRequest

```java
public class CreateDynamicRequest {
    @NotBlank(message = "动态内容不能为空")
    private String content;
    private List<String> images;
    private String location;
    
    // getters and setters
}
```

#### LikeDynamicRequest

```java
public class LikeDynamicRequest {
    @NotNull(message = "动态ID不能为空")
    private Long dynamicId;
    
    // getters and setters
}
```

#### CommentDynamicRequest

```java
public class CommentDynamicRequest {
    @NotNull(message = "动态ID不能为空")
    private Long dynamicId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    // getters and setters
}
```

#### ReplyCommentRequest

```java
public class ReplyCommentRequest {
    @NotNull(message = "评论ID不能为空")
    private Long commentId;
    
    @NotBlank(message = "回复内容不能为空")
    private String content;
    
    // getters and setters
}
```

### 6.3 响应DTO

#### GetDynamicListResponse

```java
public class GetDynamicListResponse {
    private List<DynamicDetail> dynamics;
    private long total;
    
    // getters and setters
}

public class DynamicDetail {
    private Long id;
    private String content;
    private List<String> images;
    private String location;
    private Long userId;
    private String userName;
    private String userAvatar;
    private int likes;
    private int comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### CreateDynamicResponse

```java
public class CreateDynamicResponse {
    private DynamicDetail dynamic;
    
    // getters and setters
}
```

#### GetDynamicDetailResponse

```java
public class GetDynamicDetailResponse {
    private DynamicDetail dynamic;
    
    // getters and setters
}
```

#### CommentDynamicResponse

```java
public class CommentDynamicResponse {
    private CommentDetail comment;
    
    // getters and setters
}

public class CommentDetail {
    private Long id;
    private Long dynamicId;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long parentId;
    private List<CommentDetail> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### GetDynamicCommentsResponse

```java
public class GetDynamicCommentsResponse {
    private List<CommentDetail> comments;
    private long total;
    
    // getters and setters
}
```

#### GetDynamicLikesResponse

```java
public class GetDynamicLikesResponse {
    private List<LikeDetail> likes;
    private long total;
    
    // getters and setters
}

public class LikeDetail {
    private Long id;
    private Long dynamicId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

## 7. 爱情币模块（Coins）

### 7.1 实体类

#### CoinTransaction

```java
@Entity
@Table(name = "coin_transactions")
public class CoinTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type; // 交易类型：task, reward, exchange
    
    @Column(nullable = false)
    private int amount; // 交易金额，正数为增加，负数为减少
    
    @Column(nullable = false)
    private int balance; // 交易后余额
    
    @NotBlank(message = "交易描述不能为空")
    private String description;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### Task

```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "任务标题不能为空")
    private String title;
    
    @NotBlank(message = "任务描述不能为空")
    private String description;
    
    @Column(nullable = false)
    private int reward; // 奖励金额
    
    @Column(nullable = false)
    private String status; // 任务状态：pending, completed, approved, rejected
    
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
    
    @Column(name = "executor_id")
    private Long executorId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private User creator;
    
    @ManyToOne
    @JoinColumn(name = "executor_id", insertable = false, updatable = false)
    private User executor;
    
    // getters and setters
    // 生命周期方法
}
```

#### Reward

```java
@Entity
@Table(name = "rewards")
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "奖励标题不能为空")
    private String title;
    
    @NotBlank(message = "奖励描述不能为空")
    private String description;
    
    @Column(nullable = false)
    private int price; // 奖励价格
    
    @Column(nullable = false)
    private int stock; // 奖励库存
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // getters and setters
    // 生命周期方法
}
```

### 7.2 请求DTO

#### CreateTaskRequest

```java
public class CreateTaskRequest {
    @NotBlank(message = "任务标题不能为空")
    private String title;
    
    @NotBlank(message = "任务描述不能为空")
    private String description;
    
    @NotNull(message = "奖励数量不能为空")
    @Min(value = 1, message = "奖励数量必须大于0")
    private Integer reward;
    
    // getters and setters
}
```

#### CompleteTaskRequest

```java
public class CompleteTaskRequest {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    // getters and setters
}
```

#### ApproveTaskRequest

```java
public class ApproveTaskRequest {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    @NotNull(message = "是否通过不能为空")
    private Boolean approved;
    
    // getters and setters
}
```

#### CancelTaskRequest

```java
public class CancelTaskRequest {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    // getters and setters
}
```

#### ExchangeRewardRequest

```java
public class ExchangeRewardRequest {
    @NotNull(message = "奖励ID不能为空")
    private Long rewardId;
    
    // getters and setters
}
```

### 7.3 响应DTO

#### GetCoinsBalanceResponse

```java
public class GetCoinsBalanceResponse {
    private int balance;
    
    // getters and setters
}
```

#### CreateTaskResponse

```java
public class CreateTaskResponse {
    private TaskDetail task;
    
    // getters and setters
}

public class TaskDetail {
    private Long id;
    private String title;
    private String description;
    private int reward;
    private String status;
    private Long creatorId;
    private Long executorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### GetTaskListResponse

```java
public class GetTaskListResponse {
    private List<TaskDetail> tasks;
    private long total;
    
    // getters and setters
}
```

#### GetTaskDetailResponse

```java
public class GetTaskDetailResponse {
    private TaskDetail task;
    
    // getters and setters
}
```

#### GetRewardListResponse

```java
public class GetRewardListResponse {
    private List<RewardDetail> rewards;
    private long total;
    
    // getters and setters
}

public class RewardDetail {
    private Long id;
    private String title;
    private String description;
    private int price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

#### GetRewardDetailResponse

```java
public class GetRewardDetailResponse {
    private RewardDetail reward;
    
    // getters and setters
}
```

#### GetTransactionHistoryResponse

```java
public class GetTransactionHistoryResponse {
    private List<TransactionDetail> transactions;
    private long total;
    
    // getters and setters
}

public class TransactionDetail {
    private Long id;
    private String type;
    private int amount;
    private int balance;
    private String description;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

#### GetTaskStatsResponse

```java
public class GetTaskStatsResponse {
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    
    // getters and setters
}
```

#### GetRewardStatsResponse

```java
public class GetRewardStatsResponse {
    private int totalRewards;
    private int exchangedRewards;
    
    // getters and setters
}
```

## 8. 用户模块（User）

### 8.1 实体类

#### UserProfile

```java
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    private String nickname;
    private String avatar;
    private Integer gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### UserSetting

```java
@Entity
@Table(name = "user_settings")
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "notification", columnDefinition = "boolean default true")
    private boolean notification; // 通知开关
    
    @Column(name = "theme", nullable = false, columnDefinition = "varchar(20) default 'light'")
    private String theme; // 主题设置
    
    @Column(name = "language", nullable = false, columnDefinition = "varchar(10) default 'zh-CN'")
    private String language; // 语言设置
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### UserFavorite

```java
@Entity
@Table(name = "user_favorites")
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String type; // 收藏类型：dynamic, memory, anniversary
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### UserHistory

```java
@Entity
@Table(name = "user_histories")
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String type; // 历史类型：dynamic, message, memory
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

#### UserAchievement

```java
@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String title; // 成就标题
    
    private String description; // 成就描述
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

### 8.2 请求DTO

#### UpdateProfileRequest

```java
public class UpdateProfileRequest {
    private String nickname;
    private String avatar;
    private Integer gender;
    private String birthday; // 格式：yyyy-MM-dd
    private String email;
    private String phone;
    
    // getters and setters
}
```

#### UpdateSettingsRequest

```java
public class UpdateSettingsRequest {
    private Boolean notification;
    private String theme;
    private String language;
    
    // getters and setters
}
```

#### AddFavoriteRequest

```java
public class AddFavoriteRequest {
    @NotBlank(message = "收藏类型不能为空")
    private String type;
    
    @NotNull(message = "目标ID不能为空")
    private Long targetId;
    
    // getters and setters
}
```

### 8.3 响应DTO

#### GetProfileResponse

```java
public class GetProfileResponse {
    private UserInfo userInfo;
    
    // getters and setters
}
```

#### UpdateProfileResponse

```java
public class UpdateProfileResponse {
    private UserInfo userInfo;
    
    // getters and setters
}
```

#### GetSettingsResponse

```java
public class GetSettingsResponse {
    private SettingsInfo settings;
    
    // getters and setters
}

public class SettingsInfo {
    private boolean notification;
    private String theme;
    private String language;
    
    // getters and setters
}
```

#### UpdateSettingsResponse

```java
public class UpdateSettingsResponse {
    private SettingsInfo settings;
    
    // getters and setters
}
```

#### GetFavoritesResponse

```java
public class GetFavoritesResponse {
    private List<FavoriteInfo> favorites;
    private long total;
    
    // getters and setters
}

public class FavoriteInfo {
    private Long id;
    private String type;
    private Long targetId;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

#### GetHistoriesResponse

```java
public class GetHistoriesResponse {
    private List<HistoryInfo> histories;
    private long total;
    
    // getters and setters
}

public class HistoryInfo {
    private Long id;
    private String type;
    private Long targetId;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

#### GetAchievementsResponse

```java
public class GetAchievementsResponse {
    private List<AchievementInfo> achievements;
    
    // getters and setters
}

public class AchievementInfo {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

#### GetUserStatsResponse

```java
public class GetUserStatsResponse {
    private int totalMessages;
    private int totalDynamics;
    private int totalCoins;
    
    // getters and setters
}
```

## 9. 纪念日模块（Anniversary）

### 9.1 实体类

#### Anniversary

```java
@Entity
@Table(name = "anniversaries")
public class Anniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "纪念日标题不能为空")
    private String title;
    
    @NotNull(message = "纪念日日期不能为空")
    private LocalDate date;
    
    private String description;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    // getters and setters
    // 生命周期方法
}
```

### 9.2 请求DTO

#### CreateAnniversaryRequest

```java
public class CreateAnniversaryRequest {
    @NotBlank(message = "纪念日标题不能为空")
    private String title;
    
    @NotBlank(message = "纪念日日期不能为空")
    private String date; // 格式：yyyy-MM-dd
    
    private String description;
    
    // getters and setters
}
```

#### UpdateAnniversaryRequest

```java
public class UpdateAnniversaryRequest {
    private String title;
    private String date; // 格式：yyyy-MM-dd
    private String description;
    
    // getters and setters
}
```

#### SearchAnniversaryRequest

```java
public class SearchAnniversaryRequest {
    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;
    
    // getters and setters
}
```

### 9.3 响应DTO

#### GetAnniversaryListResponse

```java
public class GetAnniversaryListResponse {
    private List<AnniversaryDetail> anniversaries;
    
    // getters and setters
}
```

#### CreateAnniversaryResponse

```java
public class CreateAnniversaryResponse {
    private AnniversaryDetail anniversary;
    
    // getters and setters
}
```

#### GetAnniversaryDetailResponse

```java
public class GetAnniversaryDetailResponse {
    private AnniversaryDetail anniversary;
    
    // getters and setters
}
```

#### UpdateAnniversaryResponse

```java
public class UpdateAnniversaryResponse {
    private AnniversaryDetail anniversary;
    
    // getters and setters
}
```

#### GetUpcomingAnniversariesResponse

```java
public class GetUpcomingAnniversariesResponse {
    private List<AnniversaryDetail> anniversaries;
    
    // getters and setters
}
```

#### GetAnniversaryStatsResponse

```java
public class GetAnniversaryStatsResponse {
    private int totalAnniversaries;
    private int upcomingAnniversaries;
    
    // getters and setters
}
```

#### SearchAnniversaryResponse

```java
public class SearchAnniversaryResponse {
    private List<AnniversaryDetail> anniversaries;
    
    // getters and setters
}
```

## 10. 壁纸模块（Wallpaper）

### 10.1 实体类

#### Wallpaper

```java
@Entity
@Table(name = "wallpapers")
public class Wallpaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "壁纸标题不能为空")
    private String title;
    
    @NotBlank(message = "壁纸URL不能为空")
    private String url;
    
    private String thumbnail;
    private String resolution;
    private String category;
    private int downloads;
    private int likes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToOne
    @JoinColumn(name = "category_id")
    private WallpaperCategory category;
    
    @OneToMany(mappedBy = "wallpaper")
    private List<WallpaperDownload> downloads;
    
    // getters and setters
    // 生命周期方法
}
```

#### WallpaperCategory

```java
@Entity
@Table(name = "wallpaper_categories")
public class WallpaperCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    private String icon;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @OneToMany(mappedBy = "category")
    private List<Wallpaper> wallpapers;
    
    // getters and setters
    // 生命周期方法
}
```

#### WallpaperSubject

```java
@Entity
@Table(name = "wallpaper_subjects")
public class WallpaperSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "专题名称不能为空")
    private String name;
    
    private String description;
    private String cover;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联关系
    @ManyToMany
    @JoinTable(
        name = "wallpaper_subject_relation",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "wallpaper_id")
    )
    private List<Wallpaper> wallpapers;
    
    // getters and setters
    // 生命周期方法
}
```

### 10.2 请求DTO

#### DownloadWallpaperRequest

```java
public class DownloadWallpaperRequest {
    @NotNull(message = "壁纸ID不能为空")
    private Long wallpaperId;
    
    // getters and setters
}
```

#### SearchWallpaperRequest

```java
public class SearchWallpaperRequest {
    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;
    private Integer page;
    private Integer pageSize;
    
    // getters and setters
}
```

### 10.3 响应DTO

#### GetRandomWallpaperResponse

```java
public class GetRandomWallpaperResponse {
    private WallpaperDetail wallpaper;
    
    // getters and setters
}

public class WallpaperDetail {
    private Long id;
    private String title;
    private String url;
    private String thumbnail;
    private String resolution;
    private String category;
    private int downloads;
    private int likes;
    private LocalDateTime createdAt;
    
    // getters and setters
}
```

#### GetWallpaperCategoriesResponse

```java
public class GetWallpaperCategoriesResponse {
    private List<CategoryDetail> classifies;
    
    // getters and setters
}

public class CategoryDetail {
    private Long id;
    private String name;
    private String description;
    private String icon;
    
    // getters and setters
}
```

#### GetWallpaperListResponse

```java
public class GetWallpaperListResponse {
    private List<WallpaperDetail> wallpapers;
    private long total;
    
    // getters and setters
}
```

#### GetWallpaperDetailResponse

```java
public class GetWallpaperDetailResponse {
    private WallpaperDetail wallpaper;
    
    // getters and setters
}
```

#### SearchWallpaperResponse

```java
public class SearchWallpaperResponse {
    private List<WallpaperDetail> wallpapers;
    private long total;
    
    // getters and setters
}
```

#### GetSubjectListResponse

```java
public class GetSubjectListResponse {
    private List<SubjectDetail> subjects;
    
    // getters and setters
}

public class SubjectDetail {
    private Long id;
    private String name;
    private String description;
    private String cover;
    
    // getters and setters
}
```

#### GetSubjectDetailResponse

```java
public class GetSubjectDetailResponse {
    private SubjectDetail subject;
    
    // getters and setters
}
```

#### GetLatestWallpapersResponse

```java
public class GetLatestWallpapersResponse {
    private List<WallpaperDetail> wallpapers;
    private long total;
    
    // getters and setters
}
```

#### GetWallpaperRankResponse

```java
public class GetWallpaperRankResponse {
    private List<WallpaperDetail> wallpapers;
    private long total;
    
    // getters and setters
}
```

## 11. 总结

本文档提供了情侣互动小程序后端开发所需的实体类、请求DTO和响应DTO模型。这些模型基于之前生成的API接口文档，覆盖了认证、情侣关系、消息、动态、爱情币、用户、纪念日和壁纸等模块。

后端开发人员可以根据本文档创建对应的Java类，并根据实际需求进行调整。在实现过程中，需要注意：

1. 使用合适的ORM框架（如Spring Data JPA）来管理实体类和数据库表的映射
2. 使用验证框架（如Hibernate Validator）来验证请求参数
3. 实现适当的异常处理和错误响应机制
4. 确保数据安全，特别是敏感信息（如密码）的加密存储
5. 优化数据库查询，避免性能问题

希望本文档对后端开发有所帮助！