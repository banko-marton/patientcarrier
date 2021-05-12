// mars robot 1



//Initial beliefs 


at(P) :- pos(P,X,Y) & pos(r0,X,Y).

+!handle(PatientId, LocTo) : handling <- !handle(PatientId, LocTo).

+!handle(PatientId, LocTo) <-
+handling;
.print("Handling patient ", PatientId, " destination: ", LocTo);
!at("Reception");
!at(LocTo);
-handling.


//Lépkedő
+!at(L) : at(L) <-
.print("Arrived at ", L);
arrived;
if(L == "Reception") {pickup;}
else {drop;}.

+!at(L) <- ?pos(L,X,Y);
           move_towards(X,Y);
           !at(L).
		   

dist(base, 10).
dist(dest, 20).


price(Task,X) :- dist(base, B) & dist(dest, D) & X = B + D.

plays(initiator,testManager).

/* Plans */
// send a message to initiator introducing myself // as a participant
+plays(initiator,In)
        :  .my_name(Me)
        <- .send(In,tell,introduction(carrier,Me)).
		
		
		
// answer a Call For Proposal
@c1 +bidPatient(CNPId,PatientId, LocTo)[source(A)]
: plays(initiator,A) & price(CNPId,Offer)
<- .print("bidnel a LocTo: ",LocTo);
+proposal(CNPId,PatientId,LocTo,Offer); // remember my proposal
           .send(A,tell,propose(CNPId,Offer)).
		   
@r1 +accept_proposal(CNPId)
: proposal(CNPId,PatientId,LocTo,Offer)
<- .print("My proposal ",Offer," won CNP ",CNPId,"!");
.print("PatientId:",PatientId," LocTo:",LocTo)
!handle(PatientId, LocTo).				  
				  
@r2 +reject_proposal(CNPId)
        <- .print("I lost CNP ",CNPId, ".");
-proposal(CNPId,_,_). // clear memory

