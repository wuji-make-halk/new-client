package com.weimingfj.api.qiniu.utils;



public final class ImageToken {
    public static final Auth dummyAuth = Auth.create("abcdefghklmnopq", "1234567890");
    /**
     * 七牛测试环境
     */
//    public static final Auth testAuth = Auth.create(
//            "u56k9Ju7JJ5udfK3FBoJ-PqEMrN_dXbJ1TNBnmuR",
//            "q6BzhArVd--G4LhqHbTXzj-JI0-E5zj5kQFPSqkH");
//    public static final String bucket = "yange";
    /**
     * 七牛正式环境
     */
    public static final Auth testAuth = Auth.create(
            "mvmVYF6IRvkxe1FdwCcIUubLIQ7A02zPCVxOYv1K",
            "E23-LQrpue4eQqR-IMu3Tm6vlN1QAS_ix0wfmbCe");
    public static final String bucket = "yunba";
    public static final String key = "java-duke.svg";
    public static final String domain = "javasdk.qiniudn.com";

    private ImageToken() {
    }

    public static boolean isTravis() {
        return "travis".equals(System.getenv("QINIU_TEST_ENV"));
    }
}
