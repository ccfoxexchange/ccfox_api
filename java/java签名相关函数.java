/***
     * HmacSha256签名,进行base64编码
     * @param signStr
     * @param secretKey
     * @return
     */
    public static String getHmacHash(String signStr, String secretKey) {
        String hash;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(signStr.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return "";
        }
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
