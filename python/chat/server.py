import grpc
from concurrent import futures
import demo_pb2
import demo_pb2_grpc

class DemoServicer(demo_pb2_grpc.DemoServiceServicer):
    def SendMessage(self, request, context):
        return demo_pb2.Response(reply=f"Received: {request.message}, server")


def run_server():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    demo_pb2_grpc.add_DemoServiceServicer_to_server(DemoServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    run_server()