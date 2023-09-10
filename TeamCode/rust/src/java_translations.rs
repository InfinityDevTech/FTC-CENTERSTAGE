/*
 *  This function is used to convert a java string to a rust bool.
 */
use jni::JNIEnv;
use jni::objects::{JObject, JString};

pub enum JavaMessage {
    Initialized,
}

pub fn convert_string_to_bool(env: &mut JNIEnv, string: &JString) -> bool {
    let string_st: String = env.get_string(string).expect("Couldn't get java string").into();
    string_st.to_uppercase() == "TRUE"
}

pub fn send_to_telemetry(env: &mut JNIEnv, class: &JObject, message: JavaMessage) {
    let string_j = match message {
        JavaMessage::Initialized => 0
    };
    env.call_method(class, "SendToTelemetry", "(I)V", &[string_j.into()]).expect("Failed to call SendToTelemetry");
}