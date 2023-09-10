use std::hint::black_box;
use std::time::Instant;
use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jstring;

// No_mangle makes it so that the rust compiler doesnt change the function name, so java can access it.
fn multiply_without_optimization(a: i64, b: i64) -> i64 {
    let mut result: i64 = 0;
    for i in 0..32 {
        result += (a * b) >> i;
    }
    result
}

#[no_mangle]
pub extern "system" fn Java_org_firstinspires_ftc_teamcode_freeWifi_Utils_RustLink_foobarTest(env: JNIEnv, _class: JClass, _silent: JString) -> jstring {
    let start = Instant::now();
    let mut total: i64 = 0;
    for i in 0..100000 {
        total = black_box(multiply_without_optimization(i as i64, i as i64));
    }
    let now = Instant::now();
    println!("Total: {}", total);
    return env.new_string(now.duration_since(start).as_nanos().to_string()).expect("Failed to make Java String!").into_raw()
}