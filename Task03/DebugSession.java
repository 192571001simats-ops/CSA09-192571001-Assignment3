import com.sun.jdi.*;import com.sun.jdi.event.*;import com.sun.jdi.request.*;
public class DebugSession {
 public static void main(String[] args)throws Exception{
  var connector=Bootstrap.virtualMachineManager().defaultConnector();var settings=connector.defaultArguments();
  settings.get("main").setValue("SalesDebug");settings.get("options").setValue("-cp "+System.getProperty("java.class.path"));
  VirtualMachine vm=connector.launch(settings);var requests=vm.eventRequestManager();
  var prepare=requests.createClassPrepareRequest();prepare.addClassFilter("SalesDebug");prepare.enable();
  var failures=requests.createExceptionRequest(null,true,true);failures.addClassFilter("java.lang.NumberFormatException");failures.enable();
  boolean done=false;while(!done){EventSet events=vm.eventQueue().remove(10000);if(events==null){vm.dispose();throw new IllegalStateException("debug timeout");}
   for(Event event:events){
    if(event instanceof ClassPrepareEvent cp){var locations=cp.referenceType().methodsByName("main").get(0).allLineLocations();var bp=requests.createBreakpointRequest(locations.get(0));bp.enable();}
    if(event instanceof BreakpointEvent bp){System.out.println("BREAKPOINT "+bp.location());var step=requests.createStepRequest(bp.thread(),StepRequest.STEP_LINE,StepRequest.STEP_OVER);step.addCountFilter(1);step.enable();}
    if(event instanceof StepEvent step){System.out.println("STEP OVER "+step.location());requests.deleteEventRequest(step.request());}
    if(event instanceof ExceptionEvent ex){System.out.println("EXCEPTION STOP "+ex.exception().referenceType().name());for(StackFrame frame:ex.thread().frames())if(frame.location().declaringType().name().equals("SalesDebug")){System.out.println("CALLER "+frame.location());for(LocalVariable variable:frame.visibleVariables())System.out.println(variable.name()+"="+frame.getValue(variable));}}
    if(event instanceof VMDeathEvent||event instanceof VMDisconnectEvent)done=true;
   }events.resume();
  }
  System.out.println("Debugger demonstrates the failure occurs before the close/reset statement");
 }
}
