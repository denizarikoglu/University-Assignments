from State import *
from socket import socket, AF_INET, SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR
from time import sleep
class Transition:
 #transiution class to initiate the funcitons
    def passive_open(self):
        print ("Error! Cannot Transition to passive open!")
        return False
    def syn(self):
        print ("Error! Cannot Transition to syn!")
        return False
    def ack(self):
        print ("Error! Cannot Transition to ack!")
        return False
    def close(self):
        print ("Error! Cannot Transition to close!")
        return False
    def fin(self):
        print ("Error! Cannot Transition to fin!")
        return False

class Closed(State, Transition):#Closed State 
    def __init__(self, Context):
        State.__init__(self, Context)
    def passive_open(self):
        print("Transitioning to Listen")
        self.CurrentContext.setState("LISTEN")#set state to "listen" 
        
    def trigger(self):
        try:
            self.CurrentContext.socket.close()#close the socket and reset to default 
            self.connection_address = 0
            print("Closing the connection.")
            return True
        except:
            return False

class Listen(State, Transition):#Listen State
    def __init__(self, Context):
        State.__init__(self, Context)
    def syn(self):
        self.CurrentContext.connection.send("SYN_ACK".encode())#send SYN_ACK encoded to byte along the connection 
        message = self.CurrentContext.connection.recv(1024)#append the message to message for later use
        sleep(self.CurrentContext.sleep_time)
        print("Sending the SYN_ACK.")
        if message.decode() == "SYN_ACK":#decode and check if correct message recieved
            print("SYN_ACK recieved.")
            self.CurrentContext.setState("SYN_RECVD")#set state to "SYN_RECVD"
            print("Transitioning to Syn_recvd")
        else:
            return False
    def trigger(self):

        if self.CurrentContext.listen() is True:#If listen funciton is true 
            print("SYN sent to " + str(self.CurrentContext.connection_address))#let the user know taht the syn was sent to the connection address
            self.CurrentContext.syn()
        else:
            return False

class Syn_Recvd(State, Transition):#SYB_RECVD State
    def __init__(self, Context):
        State.__init__(self, Context)
    def ack(self):
        message = self.CurrentContext.connection.recv(1024)
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "SYN_ACK":#Check the recieved message
            print (message + " Recieved.")
            self.CurrentContext.setState("ESTABLISHED")#Set the state to "ESTABLISHED"
            print("Transitioning to ESTABLISHED")
            
    def trigger(self):
        return self.CurrentContext.ack()

class Established(State, Transition):#Established State
    def __init__(self, Context):
        State.__init__(self, Context)
    def fin(self):
        self.CurrentContext.connection.send("ACK".encode())#Send the "ACK" encoded on teh connection
        message = self.CurrentContext.connection.recv(1024)
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "ACK":#check the sent byte after decoding 
            print("Sending the ACK.")
            self.CurrentContext.setState("CLOSE_WAIT")#set state to "CLOSE_WAIT"
            print("Transitioning to Close_wait.")
    def trigger(self):
        return self.CurrentContext.fin()

class Close_Wait(State, Transition):#Close_Wait State
    def __init__(self, Context):
        State.__init__(self, Context)
    def close(self):
        self.CurrentContext.socket.send("FIN".encode())#Send "FIN" along teh connection encoded
        message = self.CurrentContext.connection.recv(1024)
        sleep(self.CurrentContext.sleep_time)
        if message.decode() == "FIN":#Check sent message if true then set state to "LAST_ACK"
            self.CurrentContext.setState("LAST_ACK")#SET STATE 
            print("Transitioning to Last_Ack")
    def trigger(self):
        return self.CurrentContext.close()

class Last_Ack(State, Transition):#Last_Ack State
    def __init__(self, Context):
        State.__init__(self, Context)
    def ack(self):
        self.CurrentContext.setState("CLOSED")#Set the state to "CLOSED"
        print("Transitioning to Closed.")
    def trigger(self):
        return self.CurrentContext.ack()

class TCPServer(StateContext, Transition):#Constructor Class
    def __init__(self):#initiate the host and port and all available states and fucntions
        self.sleep_time = 2
        self.host = "127.0.0.1"
        self.port = 1024
        self.connection_address = 0
        self.socket = None
        self.availableStates["CLOSED"] = Closed(self)
        self.availableStates["LISTEN"] = Listen(self)
        self.availableStates["SYN_RECVD"] = Syn_Recvd(self)
        self.availableStates["ESTABLISHED"] = Established(self)
        self.availableStates["CLOSE_WAIT"] = Close_Wait(self)
        self.availableStates["LAST_ACK"] = Last_Ack(self)
        print("Transitioning to closed!")
        self.setState("CLOSED")
        
    def passive_open(self):
        return self.CurrentState.passive_open()
    def syn(self):
        return self.CurrentState.syn()
    def ack(self):
        return self.CurrentState.ack()
    def close(self):
        return self.CurrentState.close()
    def fin(self):
        return self.CurrentState.fin()
          
    def listen(self):#listen function to listen for connections 
        self.socket = socket(AF_INET, SOCK_STREAM)
        try:
            self.socket.bind((self.host, self.port))
            self.socket.listen(1)
            self.connection, self.connection_address = self.socket.accept()
            print ("Connection Address: " + self.connection_address)
            return True
        except Exception as err:
            print(err)
            exit()
      
if __name__ == '__main__':
    
   TCP = TCPServer()
   TCP.passive_open()
   
   