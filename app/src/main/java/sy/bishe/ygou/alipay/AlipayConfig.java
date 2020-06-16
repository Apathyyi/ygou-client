package sy.bishe.ygou.alipay;

    /**
     * Created by zjp on 2017/12/19 15:11.
     */
    public class AlipayConfig {
        /**
         * 支付宝支付业务：入参app_id，上文创建应用时候，已经得到
         * 由于App支付功能需要签约，因此需要上传公司信息和证件等资料进行签约
         * 以下参数，由上传完整公司信息后即可得到
         */
        public static final String APPID = "2021001140675075";
        // 商户PID
        public static final String PARTNER = "XXXXXXXXXXXXX";

        // 商户收款账号

        public static final String SELLER = "XXXXXXXXXX";
        /**
         * 支付宝账户登录授权业务：入参target_id值
         * 可以用时间戳
         */
        public static final String TARGET_ID = OrderInfoUtil2_0.getOutTradeNo();

        /** 商户私钥，pkcs8格式 */
        /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
        /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
        /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
        /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
        /**
         *  使用支付宝提供的工具生成RSA公钥和私钥
         *  工具地址：https://doc.open.alipay.com/docs/doc.htmtreeId=291&articleId=106097&docType=1
         */
        public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCM6oIXHraTx/O4fb2JG8i6xLvZVyyYnFuXATQCW9tleh+B/wVMHqUbflO2VQNimi9XmyDuk1p/h00MMsaGinChXCynN4mBNne23wipcU9AM/oAmA156C+2C0CCxADkhzJAzIMTlSTcBwfqM+iYvm6wC+GsgmeQI9sFGVuYIFqPpzXMqLnLEQi7XDZdT+RSwuYCS1pSJZG7/vg/rLarYQVFCfdbFgWGKvvOBmdGvqv+iI6BzqCLbYEp/D3WmucKFZl4P4VZVTuCtRoyQMDz/xYeOOwzaFK9Bcv8bt/TN+aZFch79bfLjylyoBcZXmqJ/nBa2XRHMn5zmw+MPX/JZexAgMBAAECggEAdqSrcRkMXbgyLOvdhafnmbMJuxnrYSWxZTX2sLeYUQyEIawW+hwF5xFbV41UOU6BaOYNDfALMOV9KRy5RlzG5tl/u1KUX6KWAayRLmVOGOuPiEP59cTOxnjocm8wNYDEOCtwRKRwDzXeer7yDXARTipoZF4Bw7Hiv0LKtsy7wNpmgHCsgokjnt0jHJ5lWiHrH9/8N4HyJ2d1CTBAivtbImx2piM+7P9ukb+179KNllHlzfa0oyYmuKv21TKbP3l5e8jWfT7kWX0Hrlp+3H8Av/UuNfn1N0+G3406Rd52R/Gs1BPK6bZxaFAqDRxyhdloCrCo/w9tekNiC4KecrtYvQKBgQC5DyFC7ygTn/r6l2djeMoqfb5riIeBbP5ayZ3aalRbggleO0QiZ5aoKtPG19u1EpsxiC6x1u+Oj7py0hkyYnMYmiG1pDD2X+PIZuC9UCTv6NkeZFAGYx+RHFVzi6JV+J7hAQzgvASspISOFRLDK66xy4a5RaeZEKAMz/Xem3TpTwKBgQC0HRYKAGcZ4+5yA+PQxJOQagrIFlreq3FkzL/I/Qoj9WChHNBoid4Xbf7kSk9R2prbAR9nbQRwH0ET6kEj4rsspu+A3SxSEhaZEu3g35Yfuf6/PsfqOwl1vmggANT2fw2d60hQH1vFyrI9pra+3nreHtQh/5jAZv9ltyU99Ksu/wKBgEVFmJnJlCLke7pZ+mWNzX9iWmk+ThrwhbXOSrx7mOA4KPGRFcwbIpYIkgWYv7FkKZu88l23qyyeOJjKdIwbtiG7cGrh70IKWuWlPPMgkPMNIljyC9KYQDS9em8qEbZEvpRTJFLrjoRhgQz+bmuIang7S08G99mgE4k4Pzz4zCaZAoGBAK+xw8Lj267Uuc98XelTKELyPiwqKmAVSxqx488yjoa/IJBo5B9lhUDDqqWUm0VUgkRqFvEz1eVth3TeyMYxsLYA7ZZ9qXMxc9vELEl0sSOnsolpu2eQSU0S/M1jIlqFW4oVbkfCHyqU7EPZcWBE9APifLnhc2cT0cSt3+VM2wOPAoGAfXOTGpX9yh+lITGRHmCbeGQHJxtf8x2skgIi/o6FlDwPPpv4u2BOkwkZvkVHOBrQJHjv03coMYIgFBJnfdXFEZr+Q5NGrlp6vRZoUzOtB5SI9rHiVImUVn0QFZH0Xv4nAhdKx5nviA3I3fyjamQRX2pVAV5+n17lHyrRpTJ5Hqw=";
        public static final String RSA_PRIVATE = "";
    }
