use crate::threads::{ThreadMessage};

pub struct MathThread {
    send: crossbeam_channel::Sender<String>,
    receive: crossbeam_channel::Receiver<String>
}
impl MathThread {
    pub fn new(send: crossbeam_channel::Sender<String>, receive: crossbeam_channel::Receiver<String>) -> Self {
        MathThread {send, receive }
    }

    pub fn start(&self) {
        println!("Math thread started!");

        loop {
            let message: ThreadMessage = serde_json::from_str(&self.receive.recv().unwrap()).unwrap();
            match message.operation.as_str() {
                "ShutDown" => {break;}
                _ => println!("Unknown operation!")
            }
        }
    }
}