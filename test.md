Cultural  
Absolutely. That is probably the best way for you to prepare tonight — I give you the question 
and only the 2–3 things you need to hit, and you formulate the answer naturally in your own 
words. 
I’ll deliberately not write the answers for you. 
1. Difficult / demanding situation 
Question: 
Tell me about one of the most difficult and demanding tasks you've had to complete. 
Hit these points: 
● 3 senior developers left + lead went overseas → you became the most experienced 
person. 
● Had 3 inexperienced developers → changed from individual contributor to creating 
structure/support for the team. 
● UAT was difficult, but you regrouped, developed people and ultimately went live 
successfully. 
2. Leadership without authority 
Question: 
Tell me about a time you had to lead people without having formal authority. 
Hit these points: 
● You weren't formally the manager; you became the de facto lead. 
● Had to create confidence and direction for inexperienced developers. 
● Focus on getting the team to succeed, rather than personally solving everything. 
3. Developing someone 
Question: 
Tell me about someone you've helped develop. 
Hit these points: 
● The talented junior developer in the 2016 project. 
● You deliberately allowed him to go to another team despite your own delivery pressure. 
● He came back with production/go-live experience and helped your team avoid problems. 
4. Difficult production problem 
Question: 
Tell me about a time you had to respond quickly to a production problem. 
Hit these points: 
● Database was failing over within minutes. 
● Investigated what had changed → VACUUM ANALYZE had changed query plans. 
● Focus on evidence rather than assuming infrastructure was the problem. 
5. Working with non-technical stakeholders 
Question: 
Tell me about a time you had to explain a technical problem to a non-technical stakeholder. 
Hit these points: 
● Business expected more documents from the account IDs they supplied. 
● Used the filing cabinet/folder analogy rather than database terminology. 
● Discovered their SOP appended additional identifiers → changed search → correct 
documents found. 
6. Handling disagreement 
Question: 
Tell me about a time someone disagreed with your approach. 
Hit these points: 
● Choose either Lambda vs EC2 or the junior developer being loaned out. 
● Explain why they disagreed — don't portray them as unreasonable. 
● Explain how you used evidence/reasoning to reach the decision. 
7. Best recommendation 
Question: 
What's one of the best recommendations you've made to a manager, peer or customer? 
Hit these points: 
● Existing thinking was EC2. 
● You looked at actual workload: 15–20 minutes processing, idle otherwise. 
● Lambda allowed decomposition/scaling and matched the workload characteristics. 
8. A decision you got wrong 
Question: 
Tell me about a decision you got wrong. 
Hit these points: 
● Added threading to Lambda as a tactical solution. 
● It worked initially but didn't scale with growing workload → technical debt. 
● In hindsight, AWS Batch would have been a better architectural direction; learned to 
distinguish tactical fix vs long-term architecture. 
9. Missed deadline 
Question: 
Tell me about a time you missed a deadline. 
Hit these points: 
● BIS batch: late discovery that 1,000-document testing didn't represent possible 
4,000-document consumption. 
● Didn't hide the problem → discussed internally and then transparently with business. 
● Created a 15-day controlled parallel/soft deployment with old system as fallback. 
10. Risk management 
Question: 
Tell me about a time you took a calculated risk. 
Hit these points: 
● BIS deployment rather than stopping everything because of the capacity issue. 
● Risk was controlled through parallel running and keeping the old system. 
● Business understood and accepted the risk because the fallback remained available. 
11. Learning from failure 
Question: 
What is something you would do differently today? 
Hit these points: 
● Capacity assumptions should have been tested earlier. 
● Tactical solutions can become technical debt if not explicitly treated as temporary. 
● You now think more about scale, failure modes and future workload, not just 
immediate delivery. 
12. Influencing without authority 
Question: 
Tell me about a time you influenced someone without having authority over them. 
Hit these points: 
● Pick Lambda/EC2 or business/document story. 
● Start by understanding their concern rather than immediately arguing. 
● Use evidence + simple explanation → get alignment. 
13. When you changed your mind 
Question: 
Tell me about a time you changed your mind because of new information. 
Hit these points: 
● Could use BIS capacity discovery. 
● Initial delivery assumption changed after discovering 4,000-document possibility. 
● Changed the rollout strategy rather than pretending the original plan was still valid. 
14. Dealing with ambiguity 
Question: 
Tell me about a situation where the requirements weren't clear. 
Hit these points: 
● Account IDs/document retrieval. 
● Initial business requirement appeared straightforward. 
● Instead of arguing over the result, brought business into the room and discovered the 
hidden SOP rule. 
15. Handling pressure 
Question: 
Tell me about a time you were under significant pressure. 
Hit these points: 
● 2016 team: senior people gone + inexperienced team + poor UAT. 
● You had to keep the team moving rather than becoming consumed by the pressure. 
● Outcome: production went live quietly despite difficult UAT. 
16. Conflict within a team 
Question: 
Tell me about a time you had to deal with disagreement within a team. 
Hit these points: 
● Use the developer-loaning example. 
● Some questioned sending a strong developer away when your own team was under 
pressure. 
● Explain your reasoning: short-term capacity sacrifice for long-term capability and 
knowledge transfer. 
17. Customer focus 
Question: 
Tell me about a time you went beyond simply delivering the technical solution. 
Hit these points: 
● BIS: didn't simply say "we can't handle 4,000." 
● Designed a business-friendly rollout with fallback. 
● Balanced technical risk with the business need to get value sooner. 
Your assessment specifically identifies client service and meeting client needs to high 
standards as a strength. 
18. What does Staff Engineer mean to you? 
Question: 
What does being a Staff Engineer mean to you? 
Hit these points: 
● Impact beyond your own code. 
● Architecture + technical decisions + influence across teams. 
● Make good engineering repetitive through patterns, guardrails and better ways of 
working. 
19. How do you measure your impact? 
Question: 
How do you know you're having an impact as a senior engineer? 
Hit these points: 
● Not just lines of code / tickets. 
● Team makes better decisions and becomes less dependent on you. 
● Business outcomes: reliability, delivery, reduced risk, performance, faster processing. 
20. Why Macquarie? 
Question: 
Why do you want to work at Macquarie? 
Hit these points: 
● Technology is important to the business. 
● Combination of technical depth + business ownership appeals to you. 
● Your progression is toward broader technical leadership, architecture and influence. 
The 5 "Director-level" questions I'd 
particularly practise 
These are the ones where I would not give a rehearsed corporate answer. 
21. What is the biggest mistake you've made as a senior engineer? 
Hit: 
● Genuine mistake. 
● Own it without blaming others. 
● What specifically changed in your behaviour afterwards. 
22. What would your manager say you need to improve? 
Hit: 
● Don't claim you're perfect. 
● Pick something around delegation / not jumping too quickly into technical detail. 
● Show what you're actively doing about it. 
This is particularly useful given the assessment's comments around leadership, mentoring and 
influence. 
23. How do you handle a decision you disagree with? 
Hit: 
● Understand the reasoning first. 
● Present evidence and challenge respectfully. 
● Once decision is made, commit unless there is a material risk requiring escalation. 
24. What happens when your team fails? 
Hit: 
● Don't immediately look for who caused it. 
● Understand systemic/root causes. 
● Own the outcome as the senior person and improve the system. 
25. Why should we trust you with a Staff-level problem? 
Hit: 
● Complex technical problems. 
● Comfortable with ambiguity and stakeholders. 
● You take ownership and can bring people together without formal authority. 
One important thing for tomorrow 
Your assessment gives you a very useful three-part story: 
Your strengths: 
● Above-average verbal reasoning. 
● Above-average abstract reasoning. 
● Strong strategic/big-picture orientation. 
Potential areas they may probe: 
● Influence. 
● Leadership/developing others. 
● Decision-making under uncertainty. 
● Resilience after setbacks. 
So if you practise only six stories, I'd choose: 
1. 2016 team → leadership / pressure / mentoring / resilience 
2. DB failover → analytical thinking / production judgement 
3. Account + documents → communication / ambiguity / business partnership 
4. BIS deployment → risk / missed deadline / stakeholder management 
5. Lambda vs EC2 → architecture / influence / recommendation 
6. Lambda threading mistake → failure / self-awareness / learning 
If you can formulate those six naturally, you can probably adapt them to most of the questions 
above without sounding like you've memorised 25 answers. 
