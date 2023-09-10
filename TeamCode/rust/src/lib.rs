/* 
*  This code will be compiled into a .dll to be used on our robot.
*  ALL, and i mean ALL functions (That need to be accessible by Java), NEED to begin with the following:
*    - Java_org_firstinspires_ftc_teamcode_freeWifi_Utils_RustLink_<FUNC NAME>
*  This should look very familiar, this is because Java likes to be funky and requires specifics.
*/
mod threads;
mod java_translations;
mod tests;

use jni::{JavaVM, JNIEnv};
use jni::objects::{JClass, JObject, JString};
use jni::sys::jstring;
use std::time::{Instant};
use crate::java_translations::send_to_telemetry;
use crate::threads::{ThreadManagement};

pub struct RustLink {
    pub thread_management: ThreadManagement,
    pub jvm: JavaVM,
}

impl RustLink {

    pub fn new(thread_management: ThreadManagement, jvm: JavaVM) -> Self {
        Self {thread_management, jvm}
    }

    #[no_mangle]
    pub extern "system" fn Java_org_firstinspires_ftc_teamcode_freeWifi_Utils_RustLink_JNISpeedTest(mut env: JNIEnv, _class: JClass, java_start: JString) -> jstring {
        let now = Instant::now();
        let millis: u128 = now.elapsed().as_millis();
        let milli: u128 = env.get_string(&java_start).expect("Couldn't get java string").to_str().unwrap().parse::<u128>().unwrap();
        return env.new_string((millis - milli).to_string()).expect("Failed to get mem").into_raw();
    }
}

// Only called once at RustLink creation.
#[no_mangle]
pub extern "system" fn Java_org_firstinspires_ftc_teamcode_freeWifi_Utils_RustLink_StartRustThreading(mut env: JNIEnv, _class: JClass, rust_link: JObject) {
    send_to_telemetry(&mut env, &rust_link, java_translations::JavaMessage::Initialized);
    let thread_management = ThreadManagement::new(4, env.get_java_vm().expect("Failed to get Java VM"));
    RustLink { thread_management, jvm: env.get_java_vm().expect("Failed to get Java VM")};
}