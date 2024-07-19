import grpc
import demo_pb2 
import demo_pb2_grpc 

def run_client():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = demo_pb2_grpc.DemoServiceStub(channel)
        response = stub.SendMessage(demo_pb2.Request(message="Hello gRPC!"))
        print(f"Response from server: {response.reply}")

if __name__ == '__main__':
    run_client()