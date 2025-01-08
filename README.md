                      WRITE_THREADS | READ_THREADS | TTL     | TEST_DURATION |
                      COUNT         | COUNT        | SECONDS | MINUTES

                      5              5              300       13
DequeConsumer:                                                                 
total req  27_995_194          
prs        35_839
MapConsumer:
total req  x            
prs        x

                      10             10             300       13                       
DequeConsumer:
total req  28_416_458 
prs        36_420
  
                      1              5              60        13                       
DynamicCountConsumer:
total req  40_216_141
prs        51_554
DequeConsumer:
total req  5_026_833
prs        6_444

                      5              1              60        13                       
DynamicCountConsumer:                                                          
total req  10_269_601            
prs        13_166
DequeConsumer:                                                                 
total req  94_623_935            
prs        121_273

                      1              1              60        13                       
DynamicCountConsumer:                                                          
total req  17_400_719            
prs        22_308
DequeConsumer:                                                                 
total req  59_687_438            
prs        76_520

                      5              5              60        13                       
DynamicCountConsumer:                                                          
total req  9_207_348             
prs        11_804
DequeConsumer:                                                                 
total req  65_605_830            
prs        84_104

                      10             10             60        13                       
DynamicCountConsumer:                                                          
total req  11_818_089            
prs        15_151
DequeConsumer:                                                                 
total req  69_408_805            
prs        88_961



put/read elements with sleep 100 ms

                      5              5         300          13                       
DynamicCountConsumer:                                                          
total req  75_429                 
prs        96
DequeConsumer:                                                                 
total req  74_472                 
prs        95
MapConsumer:
total req  75_325        
prs        96