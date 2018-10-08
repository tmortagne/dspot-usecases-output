var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":3433,"methods":[{"el":39,"sc":2,"sl":33}],"name":"EnactmentCompletionTimeJsonSerializer","sl":31}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_133":{"methods":[{"sl":33}],"name":"testResetDashboard","pass":true,"statements":[{"sl":36},{"sl":37},{"sl":38}]},"test_97":{"methods":[{"sl":33}],"name":"testAddEnactment","pass":true,"statements":[{"sl":36},{"sl":37},{"sl":38}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [133, 97], [], [], [133, 97], [133, 97], [133, 97], [], [], [], []]
