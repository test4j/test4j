Scenario: 欢迎场景一
Given give greeting 
主人【housemaster=jobs.he】， 客人【guest=mary.li】，欢迎词【greeting=welcome】,时间【date=2010-11-12】
When do greeting
欢迎
Then check greeting 
拼成的完整句子为【jobs.he welcome mary.li on 2010-11-12.】

Scenario: 欢迎场景二
Given give greeting 
 客人【guest=mary.li】，主人【housemaster=jobs.he】，欢迎词【greeting=welcome】,时间【date=2010-11-12】
When do greeting
欢迎
Then check greeting 
完整句子为【jobs.he welcome mary.li on 2010-11-12.】

Scenario: 欢迎场景三
Given give greeting 
 欢迎词【greeting=welcome】,时间【date=2010-11-12】，客人【guest=mary.li】，主人【housemaster=jobs.he】
When do greeting
欢迎
Then check greeting 
完整句子为【jobs.he welcome mary.li on 2010-11-12.】