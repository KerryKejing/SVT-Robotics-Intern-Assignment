# SVT-Robotics-Intern-Assignment
1. How to run the function  

  Approach #1:   
  (1) import the project into any IDE  
  (2) add the two external libraries in the <code>externalLibs/</code> folder   
  (3) run with following flags:   
  * <code>-loadId \<arg\></code>:   a string containing an arbitrary ID of the load which needs to be moved.  
  * <code>-x \<arg\></code>:        a number indicating the x coordinate of the load which needs to be moved.  
  * <code>-y \<arg\></code>:       a number indicating the y coordinate of the load which needs to be moved. </code>  

  Approach #2:   
  (1) cd to the <code>src</code> folder  
  (2) To compile, run the following command line:      
  <code>javac -classpath /exact/path/to/this/jar/lib/commons-cli-1.3.1.jar:/exact/path/to/this/jar/lib/json-simple-1.1.1.jar ./com/example/*.java</code>   
  (3) To run the function, run the following command line:  
  <code>java -classpath :/exact/path/to/this/jar/lib/commons-cli-1.3.1.jar:/exact/path/to/this/jar/lib/json-simple-1.1.1.jar com.example.Main -loadId \<arg\> -x \<arg\> -y \<arg\></code> 

2. Description of what features, functionality, etc. I would add next and how I would implement them.  
   Considering that the batteryLevel of the closest robot is too low to get to the load, how do we pick the best robot in this case? The implementation is bascially same as the original version, but we need to check if the current best robot can get to the load successfully given the batteryLevel and distance. If it cannot, we move over to check the second closest.
    
