mod math;

use jni::JavaVM;
use crossbeam_channel::{Sender, unbounded};
use threadpool::ThreadPool;
use serde_json;
use serde::{Deserialize, Serialize};
use crate::threads::math::MathThread;

#[derive(Serialize, Deserialize)]
pub struct ThreadMessage {
    thread: String,
    task: usize,
    operation: String
}

pub struct ThreadManagement {
    pub jvm: JavaVM,
    threads: u8,
    comms_send: Option<crossbeam_channel::Sender<String>>,
    comms_receive: Option<crossbeam_channel::Receiver<String>>,
}
impl ThreadManagement {
     pub fn new(threads: u8, jvm: JavaVM) -> Self {
         Self { jvm, threads, comms_send: None, comms_receive: None }
     }

     pub fn start_threads(&self) {
         let (send, receive) = unbounded::<String>();
         let pool = ThreadPool::new(self.threads.into());
         {
             let math_send = send.clone();
             let math_receive = receive.clone();
             pool.execute(move || {
                 let math_thread: MathThread = MathThread::new(math_send, math_receive);
                 math_thread.start();
             })
         }
     }
}