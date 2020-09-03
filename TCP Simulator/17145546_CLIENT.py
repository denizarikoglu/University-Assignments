from State import *
from socket import socket, AF_INET, SOCK_STREAM
from time import sleep
class Transition:
    #Transition class used to identify the funcitons
    def active_open(self):
        print ("Error! Cannot Transition to active open!")
        return False
    def syn(self):
        print ("Error! Cannot Transition to syn!")
        return False
    def ack(self):
        print ("Error! Cannot Transition to ack!")
        return False
    def rst(self):
        print ("Error! Cannot Transition to rst!")
        return False
    def syn_ack(self):
        print ("Error! Cannot Transition to syn ack!")
        return False
    def close(self):
        print ("Error! Cannot Transition to close!")
        return False
    def timeout(self):
        print ("Error! Cannot Transition to timeout!")
        return False
    

class Closed(State, Transition):#closed state 
    def __init__(self, Context):
        State.__init__(self, Context)
    def active_open(self):
        print("Contacting the server...")
        self.CurrentContext.make_connection()#attempt to connect to the server
        self.CurrentContext.socket.send("SYN".encode())#send the "SYN" on the socket route
        message = self.CurrentContext.socket.recv(1024)#get the message
        sleep(self.CurrentContext.sleep_time)#sleep time
        if message.decode() == "SYN":#if to check the decoded message
            self.CurrentContext.setState("SYN_SENT")#set the state 
            print("Transitioning to Syn_Sent")#let user know what state they are trasitioning to
            
    def trigger(self):#trigger method to trigger the funciton
        try:
            self.CurrentContext.socket.close()#close the socket 
            self.connection_address = 0#set address to 0 so its reset 
            print("Closing the connection.")
            return True
        except:
            return False

class Syn_Sent(State, Transition):#syn_sent state
    def __init__(self, Context):
        State.__init__(self, Context)#initialise the state
    def rst(self):
        self.CurrentContext.setState("CLOSED")#if rst is triggered transition to "CLOSED" state
        print("Transitioting to closed!")
        return True
    def timeout(self):
        self.CurrentContext.socket.close()#close the socket
        self.connection_address = 0
        print("Closing the connection.")
        self.CurrentContext.setState("CLOSED")#set state to "CLOSED"
        print("Transitioting to closed!")
        return True 
    def syn_ack(self):
        self.CurrentContext.socket.send("ACK".encode())#send "ACK" on the socket encoded so in byte 
        message = self.CurrentContext.socket.recv(1024)#get the sent byte and append to message
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "ACK":#decode message and check if correct 

            print("Sending the ACK.")
            self.CurrentContext.setState("ESTABLISHED")#transitioning to established
            print("Transitioting to Established.") 
        else:
            return self.CurrentContext.rst()#if message is wrong then call rst whgich will close the connection
        return True
    def trigger(self):
        return self.CurrentContext.syn_ack()
class Established(State, Transition):#established state
    def __init__(self, Context):
        State.__init__(self, Context)
    def close(self):
        self.CurrentContext.socket.send("FIN".encode())#send "FIN" on the socket encoded to byte
        message = self.CurrentContext.socket.recv(1024)#append the byte message to message 
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "FIN":#check if correct byte recieved after decoding 

            print("Sending FIN.")
            self.CurrentContext.setState("FIN_WAIT1")#set state to "FIN_WAIT1" 
            print("Transitioning to Fin_Wait1.")
        else:
            return False
        return True
    def trigger(self):
        return self.CurrentContext.close()
    

class Fin_Wait1(State, Transition):#FIN_WAIT1 Sate
    def __init__(self, Context):
        State.__init__(self, Context)
    def ack(self):
        self.CurrentContext.setState("FIN_WAIT2")#set state to "FIN_WAIT2" during this state
        print("Transitioning to Fin_Wait2.")
    def trigger(self):
        self.ack()

class Fin_Wait2(State, Transition):#FIN_WAIT2 State
    def __init__(self, Context):
        State.__init__(self, Context)
    def fin(self):
        self.CurrentContext.socket.send("ACK".encode())#send "ACK" encoded to byte along teh socket
        message = self.CurrentContext.socket.recv(1024)#append the message to message
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "ACK":#check if correct message recieved after decoding 

            print("Sending the ACK.")
            self.CurrentContext.setState("TIMED_WAIT")#set to "TIMED_WAIT" if true
            print("Transitioning to Timed_Wait.")
        else:
            return False
        return True
    def trigger(self):
        self.fin()

class Timed_Wait(State, Transition):#Timed_Wait State
    def __init__(self, Context):
        State.__init__(self, Context)
    def timeout(self):
        self.CurrentContext.setState("CLOSED")#set state to "CLOSED" during a timeout
        print("Transitioning to Closed as a timeout has occured.")
    def trigger(self):
        self.timeout()

class TCPClient(StateContext, Transition):#constructor class for all the states and funcitons and socket
    def __init__(self):#initialise all teh states and host and port 
        self.sleep_time = 2
        self.host = "127.0.0.1"
        self.port = 1024
        self.connection_address = 0
        self.socket = None
        self.availableStates["CLOSED"] = Closed(self)
        self.availableStates["ESTABLISHED"] = Established(self)
        self.availableStates["SYN_SENT"] = Syn_Sent(self)
        self.availableStates["FIN_WAIT1"] = Fin_Wait1(self)
        self.availableStates["FIN_WAIT2"] = Fin_Wait2(self)
        self.availableStates["TIMED_WAIT"] = Timed_Wait(self)
        print("Transitioning to closed!")
        self.setState("CLOSED")
    def active_open(self):
        return self.CurrentState.active_open()
    def rst(self):
        return self.CurrentState.rst()
    def timeout(self):
        return self.CurrentState.timeout()
    def syn_ack(self):
        return self.CurrentState.syn_ack()
    def close(self):
        return self.CurrentState.close()
    def ack(self):
        return self.CurrentState.ack()
    def syn(self):
        return self.CurrentState.syn()
    def make_connection(self):#make connection functin to connect with socket 
        '''initiates an outbound connection'''
        self.socket = socket(AF_INET, SOCK_STREAM)
        try:
            self.socket.connect((self.host, self.port))
            self.connection_address = self.host
        except Exception as err:
            print(err)
            exit()
   
if __name__ == '__main__':

    TCP = TCPClient()
    
    TCP.active_open()