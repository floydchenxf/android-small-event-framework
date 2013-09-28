android event framework

使用方法
1. 定义自己的Application,初始化生成器
>public class EventApplication extends Application {<br>
    public void onCreate() {<br>
        super.onCreate();<br>
        Builder builder = new Builder().taskExecutor(3).singleExecutor();<br>
        EventEmitterConfiguration eventEmitterConfiguration = new EventEmitterConfiguration(builder);<br>
        generator.initEventEmitter(eventEmitterConfiguration);<br>
   }<br>
}

2.在自己的Activity中定义和注册事件
>protected void onCreate(Bundle savedInstanceState) {<br>
     eventEmitter = EventEmitterGenerator.getInstance().generateEmitter();<br>
     EventCallback errorCallback = new EventCallback() {<br>
	public void callback(Object object) {<br>
	    Object[] ll = (Object[]) object;<br>
	    String result = (String) ll[1];<br>
	    resultView.setText(result);<br>
	}<br>
    );<br>
    //如果是单纯的后台任务，可以不设置callback<br>
    Event errorEvent = Event.createInstance().setNextHandler(new ErrorEventHandler()).setEventCallback(errorCallback);<br>
    //注册事件<br>
    eventEmitter.regEvent(HandlerContants.ALLOT_EVENT_HANDLER, event);<br>
    eventEmitter.regEvent(HandlerContants.ERROR_EVENT_HANDLER, errorEvent);<br>
}

3.触发事件
>eventEmitter.fireEvent(HandlerContants.ALLOT_EVENT_HANDLER, new Object[] { host, userAgent });<br>

4.你也可以在自己的handle中调用别的事件（当然这个事件需要先注册）
>public class AllotEventHandler implements EventHandler<String> {<br>
	...<br>
	@Override<br>
	public void handler(EventContext eventContext,Object[] args) {<br>
	  ...<br>
		HttpSocketRequest request = new HttpSocketRequest(HOST, PORT,
				sb.toString());<br>
		String response = request.sendRequest();<br>
		if (response == null) {<br>
			eventContext.invokeError(200, "获取allot失败");
		}<br>
		eventContext.invokeNext(response);
	}<br>
}
