ttl=3 sec


### acceptThread:1  meanThread:1
|                     | RUNNABLE | TIMED_WAITING | WAITING | testConsumerAcceptMethod   | testConsumerMeanMethod   |
|---------------------|----------|---------------|---------|----------------------------|--------------------------|
|DequeConsumer        | 60,0%    | 20,0%         | 20,0%   |  279,259 ±   5,163  ops/s  |   0,914 ±   0,033  ops/s |
|MapConsumer          | 78,4%    | 21,6%         |         | 3782,671 ± 856,390  ops/s  |   0,135 ±   0,148  ops/s |
|DynamicCountConsumer | 10,7%    | 6,6%          | 82,7%   |  113,396 ±  16,107  ops/s  | 120,207 ±  26,100  ops/s |

### acceptThread:1  meanThread:5
|                      | RUNNABLE | TIMED_WAITING | WAITING | testConsumerAcceptMethod   | testConsumerMeanMethod   |
|----------------------|----------|---------------|---------|----------------------------|--------------------------|
| DequeConsumer        | 43,1%    | 11,1%         | 45,7%   |  161,784 ±  23,838  ops/s  |   2,841 ±   0,259  ops/s |
| MapConsumer          | 85,9%    | 13,1%         | 0,9%    | 2806,024 ± 731,047  ops/s  |  57,653 ± 412,370  ops/s |
| DynamicCountConsumer | 8,7%     | 6,9%          | 84,5%   |   52,828 ±   3,085  ops/s  | 264,125 ±  15,429  ops/s |

### acceptThread:5  meanThread:1
|                      | RUNNABLE | TIMED_WAITING | WAITING | testConsumerAcceptMethod   | testConsumerMeanMethod    |
|----------------------|----------|---------------|---------|----------------------------|---------------------------|
| DequeConsumer        | 33,4%    | 11,1%         | 55,5%   |  404,471 ±   39,669  ops/s | 0,603 ±    0,019  ops/s   |
| MapConsumer          | 86,3%    | 13,4%         | 0,3%    | 4558,336 ±  501,244  ops/s | 900,819 ± 3728,496  ops/s |
| DynamicCountConsumer | 9,0%     | 5,6%          | 85,4%   |  159,321 ±   25,210  ops/s | 31,865 ±    5,068  ops/s  |

### acceptThread:5  meanThread:5
|                       | RUNNABLE | TIMED_WAITING | WAITING | testConsumerAcceptMethod   | testConsumerMeanMethod   |
|-----------------------|----------|---------------|---------|----------------------------|--------------------------|
| DequeConsumer         | 26,3%    | 7,7%          | 66,0%   | 267,791 ± 46,644  ops/s    |   1,061 ±  0,520  ops/s  |
|  DynamicCountConsumer | 7,9%     | 5,3%          | 86,8%   | 115,836 ±  2,025  ops/s    | 115,833 ±  1,980  ops/s  |
