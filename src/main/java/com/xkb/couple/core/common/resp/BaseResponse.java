package com.xkb.couple.core.common.resp;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.constants.SystemConfigConstans;
import lombok.Builder;
import lombok.Data;
/**
 * 响应结果
 * @param <T>
 *  @author xuwatermelon
 *  @date 2026/02/06
 */
@Data
@Builder
public class BaseResponse <T>{
        /**
         * 状态码
         */
        private String code;
        /**
         * 消息
         */
        private String message;
        /**
         * 数据
         */
        private T data;

        /**
         * 成功响应
         * @param data 数据
         * @param <T>
         * @return BaseResponse
         */
        public static <T> BaseResponse<T> success(T data) {
                return BaseResponse.<T>builder()
                        .code(ErrorCodeEnum.SUCCESS.getCode())
                        .message(ErrorCodeEnum.SUCCESS.getMessage())
                        .data(data)
                        .build();
        }

        /**
         * 成功响应（无数据）
         * @param <T>
         * @return BaseResponse
         */
        public static <T> BaseResponse<T> success() {
                return success(null);
        }

        /**
         * 失败响应（使用错误码枚举）
         * @param errorCodeEnum 错误码枚举
         * @param <T>
         * @return BaseResponse
         */
        public static <T> BaseResponse<T> fail(ErrorCodeEnum errorCodeEnum) {
                return BaseResponse.<T>builder()
                        .code(errorCodeEnum.getCode())
                        .message(errorCodeEnum.getMessage())
                        .build();
        }

        /**
         * 失败响应（自定义消息）
         * @param message 自定义消息
         * @param <T>
         * @return BaseResponse
         */
        public static <T> BaseResponse<T> fail(String message) {
                return BaseResponse.<T>builder()
                        .code(ErrorCodeEnum.FAIL.getCode())
                        .message(message)
                        .build();
        }
}
