This is a HowTo page that describes how to use LinkSet.

# Listener provider #

This code shows how to implement a class that can be observed by listeners.

```
package com.mycompany.project1;

import org.linkset.DefaultListenerManager;
import org.linkset.ListenerManager;
import org.linkset.MethodPointer;

public class ListenerProvider {

    // a multi- listener manager
    private final DefaultListenerManager clickListeners = new DefaultListenerManager();
    // a single listener pointer - useful when a return value is needed
    private MethodPointer vetoablePointer;

    // some constants
    public final static int LeftButton = 0;
    public final static int RightButton = 1;

    public ListenerManager clickdListeners() {
        
        return this.clickListeners;
    }

    public void setVetoablePointer(MethodPointer pointer) {

        this.vetoablePointer = pointer;
    }

    public void doStuff() throws Exception {

        //fire click event
        this.clickListeners.invokeAll(LeftButton);

        // check if we can change state if we want to
        final boolean canChangeState = (Boolean)this.vetoablePointer.invoke();
        if(canChangeState == true) {

        }
    }
}
```

# Listener handler #

This code shows how to implement a class that provides methods that listen to events.

```
package com.mycompany.project1;

import org.linkset.HandlerMethod;
import org.linkset.MethodPointer;

public class HandlerProvider {

    private ListenerProvider provider = new ListenerProvider();

    public HandlerProvider() {

        // we set a reference to an object and handler method id
        this.provider.clickdListeners().add(this, "clickListener");

        // in case of static methods we need to pass a Class object reference
        this.provider.setVetoablePointer(new MethodPointer(this.getClass(), "canChange"));
    }

    @HandlerMethod(id = "clickListener")
    private void clickListener(int button) {

        System.out.println("Button click=" + button);
    }

    @HandlerMethod(id = "canChange")
    private static boolean canChange() {

        System.out.println("Can change?");
        return false;
    }

    public static void main(String[] args) throws Exception {

        HandlerProvider handler = new HandlerProvider();
        handler.provider.doStuff();

    }
}
```

And that is all You need to know :)