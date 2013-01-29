
Deadlock: bankers safty algorithm.

the input of this program is a text file named "s.txt"

format: 



<entry> <entry> <entry> ... -1       

process#        resource_allocation           res_max_needed               available_res
               res1   res2    res3...     res1    res2    res3...       res1   res2    res3...
--------    --------------------------   ------------------------     --------------------------
<entry>      <entry> <entry> <entry>...  <entry> <entry> <entry>...   <entry> <entry> <entry>...
<entry>      <entry> <entry> <entry>...  <entry> <entry> <entry>...   
<entry>      <entry> <entry> <entry>...  <entry> <entry> <entry>...   
   .            .       .       .           .       .       .            
   .            .       .       .           .       .       .           
   .            .       .       .           .       .       .        
-1


first part:
 1- each <entry> is a resource and the <entry> itself (the value) is # of instances.
 2- (-1) is to end the resource input .
 Note: entries MUST BE integers

Second Part:
 1- you just have to write the entries (under "-------------")
 2- (-1) is to end the processes input.
 3- (available_res) is an one dimentional vector so you must enter
is only with the first process (first line of part two)
 Note: entries MUST BE integers.

Note: Number of spaces does not matter.





