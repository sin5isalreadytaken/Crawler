
import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule; 

import java.util.Date;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;  
//QuartzDemo ---- Schedule和trigger测试类
/*
public class Test {
	public void run() throws SchedulerException{
		System.out.println("初始化调度工厂**********");
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		System.out.println("初始化完成");
		//Date runTime = evenMinuteDate(new Date());
		Date startTime = DateBuilder.nextGivenSecondDate(null,15); 
		Date endTime = DateBuilder.nextGivenMinuteDate(null, 1);
		JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();
		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1")  
		        .startAt(startTime)   
		        //.endAt(endTime)
		        .withSchedule(  
		                simpleSchedule()  
		                .withIntervalInSeconds(2)  
		                .repeatForever()  
		        )                     
		        .build(); 
		//Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + startTime);
		sched.start(); // startTime的作用
		//System.out.println("Wait 60 seconds");
		//try{
			//Thread.sleep(60L * 1000L);
		//}catch(Exception e){
			//e.printStackTrace();
		//}
		sched.shutdown();
		System.out.println("Sched shut down Complete!!");
	}
	

	public static void main(String[] args) {
		Test test = new Test();
		try {
			test.run();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		

	}
}
*/
