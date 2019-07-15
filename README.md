# consoleChat_v2 Code review

- Я думаю, что Enum подходит только для хранения логически связанных констант, а для остального нужно использовать классы. В данном случае, если нужен только один экземпляр, можно было бы реализовать паттерн Singleton.

- by.touchsoft.vasilyevanatali.Thread.ConversationHandlerThread - 63 line

- by/touchsoft/vasilyevanatali/Model/User.java 99 line Нужно сделать конструктор по умолчанию, туда вынести this.userId = UserIdGenerator.createID(); и затем вызывать его вначале другого конструктора.

- by/touchsoft/vasilyevanatali/Util/ChatIdGenerator.java и UserIdGenerator.java там будет достаточно одного метода generateId(), потому что использования второго метода я не нашел

- by/touchsoft/vasilyevanatali/Model/Chatroom.java Помойму тут хватило бы и односторонней очереди ConcurrentLinkedQueue

- by/touchsoft/vasilyevanatali/Controller/RestController.java 302-311 Можно обойтись одним циклом, потому что remove возвращает удаляемый объект forUserMessages.add(chatroom.getMessages().remove(message));
И лучше еще сделать метод в Chatroom, который возвращал бы сообщения по id, вместо того, что бы в 297 строке создавать еще один список


