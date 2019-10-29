    /***
     * HmacSha256签名,转换成字符串
     *
     * @param message  "verb + path + expires + data"
     * @param apiKey 秘钥
     * @return String
     */
    public static String getHmacHashForSignature(String apiKey, String message) throws Exception {
        String hash;
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec apiKeySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(apiKeySpec);
        hash = byteArrayToHexString(hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        return hash;
    }


/**
     * 检查签名，如合法，则返回userApiKeyPo
     *
     * @param apiKey    apikey
     * @param expires   过期时间戳
     * @param path      路径
     * @param signature 签名
     * @param data      "{'s': 123}"
     * @param verb      GET|POST|DELETE|PUT
     */
    @Override
    public ResultVo check(String apiKey, String signature, String verb, String path, long expires, String data) {
        // 签名
        String newSignature = null;
        try {
            newSignature = EncryptUtil.getHmacHashForSignature(this.decodeSecretKey(userAPIKeyPo.getSecretKey()), verb + path + expires + data);
        } catch (Exception e) {
            logger.error("签名错误：" + e.getMessage());
            
        }
    }


    /**
     * 检查资源名称
     *
     * @param verb GET|POST|DELETE|PUT
     * @return bool
     */
    @Override
    public boolean checkVerb(String verb) {
        String[] arr = {"GET", "POST", "DELETE", "PUT"};
        return Arrays.asList(arr).contains(verb);
    }

    /**
     * 解密secretKey
     *
     * @param encryptSecretKey 加密过的secretKey
     * @return String
     */
    @Override
    public String decodeSecretKey(String encryptSecretKey) {
        return encryptClient.decrypt(encryptSecretKey);
    }
