package com.kvstore.store;

public class Test {
    public static void main(String[] args) throws Exception {
        StorageEngine engine = new StorageEngine();

        engine.put("a", "10", -1);
        System.out.println(engine.get("a"));

        engine.put("b", "20", 1000);
        Thread.sleep(1500);
        System.out.println(engine.get("b"));

        System.out.println(engine.exists("a"));
        engine.delete("a");
        System.out.println(engine.exists("a"));
    }
}