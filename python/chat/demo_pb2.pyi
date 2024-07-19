from google.protobuf import message as _message
from typing import ClassVar as _ClassVar,Optional as _Optional

from google.protobuf import descriptor as _descriptor
DESCRIPTOR: _descriptor.FileDescriptor

class Request(_message.Message):
    __slots__ = ("message",)
    MESSAGE_FIELD_NUMBER = _ClassVar[int]
    message: str
    def __init__(self, name: _Optional[str] = ...) -> None: ...

class Response(_message.Message):
   __slots__ = ("reply",)
   REPLY_FIELD_NUMBER = _ClassVar[int]
   reply: str
   def __init__(self, reply: _Optional[str] = ...) -> None: ...

   