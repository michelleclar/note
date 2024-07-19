```sh
conda create -n chat python=3.10

-- gen grpc
python -m grpc_tools.protoc -I../../protos --python_out=. --grpc_python_out=. ../../protos/demo.proto

```